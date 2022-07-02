package com.arsvechkarev.vault.core.mvi.tea

import com.arsvechkarev.vault.core.DispatchersFacade
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TeaStoreImpl<State : Any, Event : Any, UiEvent : Event, Command : Any, News : Any>(
  private val actors: List<Actor<Command, Event>>,
  private val reducer: Reducer<State, Event, Command, News>,
  initialState: State,
) : TeaStore<State, UiEvent, News> {
  
  private val commandsFlow = MutableSharedFlow<Command>()
  private val eventsFlow = MutableSharedFlow<Event>()
  
  override val stateFlow = MutableStateFlow(initialState)
  override val news = MutableSharedFlow<News>()
  
  override fun launch(coroutineScope: CoroutineScope, dispatchersFacade: DispatchersFacade) {
    actors.forEach { actor ->
      coroutineScope.launch(dispatchersFacade.IO) {
        println("launched for actor $actor")
        try {
          actor.handle(commandsFlow)
              .onEach { event -> println("actor processing event $event") }
              .collect { event ->
                eventsFlow.emit(event)
                println("actor emmited event $event")
              }
        } catch (e: CancellationException) {
          println("throwing $e")
          throw e
        }
      }
    }
    coroutineScope.launch {
      println("launched events flow")
      eventsFlow.collect { event ->
        println("processing event $event")
        val currentState = stateFlow.value
        val update = reducer.reduce(currentState, event)
        println("update = $update")
        if (update.state != currentState) {
          println("update = $update")
          withContext(dispatchersFacade.Main) {
            println("dispatching state ${update.state}")
            stateFlow.emit(update.state)
          }
        }
        update.commands.forEach { command ->
          println("emitting command $command")
          commandsFlow.emit(command)
        }
        withContext(dispatchersFacade.Main) {
          println("emitting news")
          update.news.forEach { command -> news.emit(command) }
        }
      }
    }
  }
  
  override fun dispatch(event: UiEvent) {
    println("trying to dispatch event $event")
    val result = eventsFlow.tryEmit(event)
    println("dispatch successful = $result")
  }
}
