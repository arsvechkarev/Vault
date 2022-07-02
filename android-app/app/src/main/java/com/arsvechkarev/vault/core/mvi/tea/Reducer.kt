package com.arsvechkarev.vault.core.mvi.tea

fun interface Reducer<State : Any, Event : Any, Command : Any, News : Any> {
  
  fun reduce(state: State, event: Event): Update<State, Command, News>
}

data class Update<State : Any, Command : Any, News : Any>(
  val state: State,
  val commands: List<Command> = emptyList(),
  val news: List<News> = emptyList(),
)

