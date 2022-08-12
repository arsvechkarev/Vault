package com.arsvechkarev.vault.core.mvi.tea

import android.util.Log
import com.arsvechkarev.vault.core.DispatchersFacade
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TeaStoreImpl<State : Any, Event : Any, UiEvent : Event, Command : Any, News : Any>(
  private val actors: List<Actor<Command, Event>>,
  private val reducer: Reducer<State, Event, Command, News>,
  initialState: State,
) : TeaStore<State, UiEvent, News> {
  
  private val commandsFlow = MutableSharedFlow<Command>(replay = 1)
  
  private val eventsFlow = MutableSharedFlow<Event>(
    replay = 1,
    extraBufferCapacity = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST,
  )
  
  override val state = MutableStateFlow(initialState)
  override val news = MutableSharedFlow<News>()
  
  override fun launch(coroutineScope: CoroutineScope, dispatchersFacade: DispatchersFacade) {
    actors.forEach { actor ->
      coroutineScope.launch(dispatchersFacade.IO) {
        try {
          actor.handle(commandsFlow)
              .collect(eventsFlow::emit)
        } catch (e: CancellationException) {
          Log.d("TeaStoreImpl", "error, throwing $e", e)
          throw e
        }
      }
    }
    coroutineScope.launch(dispatchersFacade.Default) {
      eventsFlow.collect { event ->
        val currentState = state.value
        val update = reducer.reduce(currentState, event)
        if (update.state != currentState) {
          withContext(dispatchersFacade.Main) {
            state.emit(update.state)
          }
        }
        update.commands.forEach { command ->
          commandsFlow.emit(command)
        }
        withContext(dispatchersFacade.Main) {
          update.news.forEach { newsItem ->
            news.emit(newsItem)
          }
        }
      }
    }
  }
  
  override suspend fun dispatch(event: UiEvent) {
    eventsFlow.emit(event)
  }
  
  override fun tryDispatch(event: UiEvent) {
    eventsFlow.tryEmit(event)
  }
}
