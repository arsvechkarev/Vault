package com.arsvechkarev.vault.core.mvi.tea

import com.arsvechkarev.vault.core.DispatchersFacade
import com.arsvechkarev.vault.core.mvi.tea.TeaTest.Command.LoadData
import com.arsvechkarev.vault.core.mvi.tea.TeaTest.UiEvent.OnLoadClick
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Test

class TeaTest {
  
  private data class State(val data: String)
  
  private sealed interface Event {
    class LoadedData(val data: String) : Event
    class Error(val error: Throwable) : Event
  }
  
  private sealed interface UiEvent : Event {
    object OnLoadClick : UiEvent
  }
  
  private sealed interface Command {
    object LoadData : Command
  }
  
  private sealed interface News {
    class ShowNotification(val data: String) : News
  }
  
  private fun actor(
    mapper: suspend (Command) -> Event?
  ): Actor<Command, Event> = object : Actor<Command, Event> {
    override fun handle(commands: Flow<Command>): Flow<Event> {
      return commands.mapNotNull(mapper)
    }
  }
  
  private fun reducer(
    update: (State, Event) -> Update<State, Command, News>
  ): Reducer<State, Event, Command, News> = Reducer { state, event -> update(state, event) }
  
  private val testDispatchersFacade = object : DispatchersFacade {
    override val IO = Dispatchers.Unconfined
    override val Default = Dispatchers.Unconfined
    override val Main = Dispatchers.Unconfined
  }
  
  @Test
  fun storeTest() = runBlocking {
    val scope = CoroutineScope(SupervisorJob())
    val initialState = State("")
    val loadedValue = "sfd"
    
    val actor = actor { command ->
      if (command is LoadData) Event.LoadedData(loadedValue) else null
    }
    
    val reducer = reducer { state, event ->
      if (event is OnLoadClick) {
        Update(state, commands = listOf(LoadData), emptyList())
      } else {
        Update(state, emptyList(), emptyList())
      }
    }
    
    val store = TeaStoreImpl<State, Event, UiEvent, Command, News>(listOf(actor), reducer,
      initialState)
    
    store.launch(scope, testDispatchersFacade)
    
    assertEquals(initialState, store.stateFlow.value)
    
    store.dispatch(OnLoadClick)
    
    assertEquals(State(loadedValue), store.stateFlow.value)
  }
}