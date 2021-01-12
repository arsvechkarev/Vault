package com.arsvechkarev.vault.core.extensions

inline fun <T : Any> T?.ifNotNull(block: (T) -> Unit) {
  this?.apply(block)
}

inline fun <T : Any> T.ifTrue(condition: (T) -> Boolean, block: T.() -> Unit) {
  if (condition(this)) apply(block)
}