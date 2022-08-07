package com.arsvechkarev.vault.core.mvi.tea

import android.util.Log
import com.arsvechkarev.vault.core.DispatchersFacade
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger

class TeaStoreImpl<State : Any, Event : Any, UiEvent : Event, Command : Any, News : Any>(
  private val actors: List<Actor<Command, Event>>,
  private val reducer: Reducer<State, Event, Command, News>,
  initialState: State,
) : TeaStore<State, UiEvent, News> {
  
  private val commandsFlow = MutableSharedFlow<Command>()
  
  private val eventsFlow = MutableSharedFlow<Event>(
    extraBufferCapacity = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST,
  )
  
  override val state = MutableStateFlow(initialState)
  override val news = MutableSharedFlow<News>()
  
  private val actorsLatch = CompletableDeferred<Unit>()
  private val eventsLatch = CompletableDeferred<Unit>()
  
  private val counter = AtomicInteger(actors.size)
  
  override fun launch(coroutineScope: CoroutineScope, dispatchersFacade: DispatchersFacade) {
    Log.d("TeaStoreImpl", "=====================================")
    Log.d("TeaStoreImpl", "========== launching store ==========")
    Log.d("TeaStoreImpl", "=====================================")
    actors.forEach { actor ->
      coroutineScope.launch(dispatchersFacade.IO) {
        try {
          actor.handle(commandsFlow)
              .shareIn(coroutineScope, started = WhileSubscribed(replayExpirationMillis = 0))
              .onSubscription {
                if (counter.decrementAndGet() == 0) {
                  Log.d("TeaStoreImpl",
                    "completing actors latch, subs = ${commandsFlow.subscriptionCount.value}")
                  actorsLatch.complete(Unit)
                }
              }
              .collect(eventsFlow::emit)
        } catch (e: CancellationException) {
          Log.d("TeaStoreImpl", "error, throwing $e")
          throw e
        }
      }
    }
    coroutineScope.launch(dispatchersFacade.Default) {
      Log.d("TeaStoreImpl", "starting collecting events")
      eventsFlow.onSubscription {
        Log.d("TeaStoreImpl", "events onSubscription")
        eventsLatch.complete(Unit)
      }.collect { event ->
        Log.d("TeaStoreImpl", "processing event $event")
        val currentState = state.value
        val update = reducer.reduce(currentState, event)
        if (update.state != currentState) {
          withContext(dispatchersFacade.Main) {
            state.emit(update.state)
          }
        }
        update.commands.forEach { command ->
          Log.d("TeaStoreImpl",
            "emitting command ${command.javaClass.simpleName}, subs = " +
                "${commandsFlow.subscriptionCount.value}")
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
    Log.d("TeaStoreImpl",
      "before dispatching event $event, actorsLatch = ${actorsLatch.isCompleted}, eventsLatch = ${eventsLatch.isCompleted}")
    actorsLatch.await()
    eventsLatch.await()
    Log.d("TeaStoreImpl", "dispatching event $event")
    eventsFlow.emit(event)
  }
  
  override fun tryDispatch(event: UiEvent) {
    eventsFlow.tryEmit(event)
  }
}
