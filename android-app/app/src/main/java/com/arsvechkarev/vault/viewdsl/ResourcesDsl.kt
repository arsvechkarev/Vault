@file:Suppress("ObjectPropertyName")

package com.arsvechkarev.vault.viewdsl

inline val Int.dp: Int get() = (Densities.density * this).toInt()

inline val Int.sp: Float get() = Densities.scaledDensity * this