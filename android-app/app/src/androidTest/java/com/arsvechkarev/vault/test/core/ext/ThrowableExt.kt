package com.arsvechkarev.vault.test.core.ext

import io.reactivex.exceptions.ExtCompositeException

/**
 * @return true if the given throwable is contained by [allowed] set, false otherwise.
 */
internal fun <T : Throwable> T.isAllowed(allowed: Set<Class<out Throwable>>): Boolean {
  return when (this) {
    is ExtCompositeException -> {
      exceptions.find { e: Throwable ->
        allowed.find { it.isAssignableFrom(e.javaClass) } != null
      } != null
    }
    
    else -> {
      allowed.find { it.isAssignableFrom(javaClass) } != null
    }
  }
}
