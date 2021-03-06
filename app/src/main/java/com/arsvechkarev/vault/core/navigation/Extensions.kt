package com.arsvechkarev.vault.core.navigation

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun assertThat(condition: Boolean, lazyMessage: () -> String = { "" }) {
  contract {
    callsInPlace(lazyMessage, InvocationKind.EXACTLY_ONCE)
    returns() implies condition
  }
  if (!condition) {
    throw AssertionError(lazyMessage())
  }
}