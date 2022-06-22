package com.arsvechkarev.vault.core.mvi.tea

fun interface Reducer<State, Event, Command> {
  
  fun reduce(state: State, event: Event): State
}
