@file:Suppress("ObjectPropertyName")

package com.arsvechkarev.viewdsl

import com.arsvechkarev.vault.core.viewdsl.Densities

object Floats {
  inline val Int.dp: Float get() = Densities.density * this
}

object Ints {
  inline val Int.dp: Int get() = (Densities.density * this).toInt()
}