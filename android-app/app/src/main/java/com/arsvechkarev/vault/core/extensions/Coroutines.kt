package com.arsvechkarev.vault.core.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest

inline fun <Command, Event> Flow<Command>.emptyMap(
  crossinline action: suspend (Command) -> Unit
): Flow<Event> {
  return flatMapLatest { action(it); emptyFlow() }
}
