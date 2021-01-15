@file:Suppress("ObjectPropertyName")

package com.arsvechkarev.vault.core.viewdsl

object Floats {
  inline val Int.dp: Float get() = Densities.density * this
}

object Ints {
  inline val Int.dp: Int get() = (Densities.density * this).toInt()
}