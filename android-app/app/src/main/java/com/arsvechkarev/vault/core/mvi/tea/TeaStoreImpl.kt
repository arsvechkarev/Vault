package com.arsvechkarev.vault.core.mvi.tea

import android.util.Log
import com.arsvechkarev.vault.core.DispatchersFacade
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger

class TeaStoreImpl<State : Any, Event : Any, UiEvent : Event, Command : Any, News : Any>(
  private val actors: List<Actor<Command, Event>>,
  private val reducer: Reducer<State, Event, Command, News>,
  initialState: State,
) : TeaStore<State, UiEvent, News> {
  
  private val commandsChannel = Channel<Command>(capacity = Channel.UNLIMITED)
  private val eventsChannel = Channel<Event>(capacity = Channel.UNLIMITED)
  
  override val state = MutableStateFlow(initialState)
  override val news = MutableSharedFlow<News>()
  
  private val counter = AtomicInteger(actors.size)
  
  private val deferred = CompletableDeferred<Unit>()
  
  override fun launch(coroutineScope: CoroutineScope, dispatchersFacade: DispatchersFacade) {
    Log.d("TeaStoreImpl", "store launched")
    val cf = commandsChannel.consumeAsFlow().shareIn(coroutineScope, SharingStarted.Eagerly)
    actors.forEach { actor ->
      coroutineScope.launch(dispatchersFacade.IO) {
        Log.d("TeaStoreImpl", "launched for actor $actor")
        try {
          actor.handle(cf)
              .onStart {
                Log.d("TeaStoreImpl", "counting down for actor $actor")
                if (counter.decrementAndGet() == 0) {
                  Log.d("TeaStoreImpl", "completing")
                  deferred.complete(Unit)
                }
              }
              .onEach { Log.d("TeaStoreImpl", "receiving command $it") }
              .collect { event ->
                Log.d("TeaStoreImpl", "actor emitting event $event")
                eventsChannel.send(event)
              }
        } catch (e: CancellationException) {
          Log.d("TeaStoreImpl", "error, throwing $e")
          throw e
        }
      }
    }
    coroutineScope.launch(dispatchersFacade.Default) {
      Log.d("TeaStoreImpl", "launched events flow")
      @Suppress("BlockingMethodInNonBlockingContext")
      deferred.await()
      while (isActive) {
        val event = eventsChannel.receive()
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
          commandsChannel.send(command)
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
    Log.d("TeaStoreImpl", "tryEmit = ${eventsChannel.trySend(event).isSuccess}")
  }
}
