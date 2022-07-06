package com.arsvechkarev.vault.core.mvi.tea

import android.util.Log
import com.arsvechkarev.vault.core.DispatchersFacade
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TeaStoreImpl<State : Any, Event : Any, UiEvent : Event, Command : Any, News : Any>(
  private val actors: List<Actor<Command, Event>>,
  private val reducer: Reducer<State, Event, Command, News>,
  initialState: State,
) : TeaStore<State, UiEvent, News> {
  
  private val commandsFlow = MutableSharedFlow<Command>()
  private val eventsFlow = MutableSharedFlow<Event>(
    extraBufferCapacity = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
  )
  
  override val state = MutableStateFlow(initialState)
  override val news = MutableSharedFlow<News>()
  
  override fun launch(coroutineScope: CoroutineScope, dispatchersFacade: DispatchersFacade) {
    Log.d("TeaStoreImpl", "store launched")
    actors.forEach { actor ->
      coroutineScope.launch(dispatchersFacade.IO) {
        Log.d("TeaStoreImpl", "launched for actor $actor")
        try {
          actor.handle(commandsFlow)
              .collect { event ->
                Log.d("TeaStoreImpl", "actor emitting event $event")
                eventsFlow.emit(event)
              }
        } catch (e: CancellationException) {
          Log.d("TeaStoreImpl", "error, throwing $e")
          throw e
        }
      }
    }
    coroutineScope.launch(dispatchersFacade.Default) {
      Log.d("TeaStoreImpl", "launched events flow")
      eventsFlow.collect { event ->
        Log.d("TeaStoreImpl", "processing event $event")
        val currentState = state.value
        val update = reducer.reduce(currentState, event)
        Log.d("TeaStoreImpl", "update = $update")
        if (update.state != currentState) {
          withContext(dispatchersFacade.Main) {
            Log.d("TeaStoreImpl", "dispatching state ${update.state}")
            state.emit(update.state)
          }
        }
        update.commands.forEach { command ->
          Log.d("TeaStoreImpl", "emitting command $command")
          commandsFlow.emit(command)
        }
        withContext(dispatchersFacade.Main) {
          update.news.forEach { newsItem ->
            Log.d("TeaStoreImpl", "emitting news $newsItem")
            news.emit(newsItem)
          }
        }
      }
    }
  }
  
  override fun dispatch(event: UiEvent) {
    Log.d("TeaStoreImpl", "dispatching event $event")
    eventsFlow.tryEmit(event)
  }
}
