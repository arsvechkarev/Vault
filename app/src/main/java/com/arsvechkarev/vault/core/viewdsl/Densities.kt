@file:Suppress("ObjectPropertyName")

package com.arsvechkarev.vault.core.viewdsl

import android.content.res.Resources

object Densities {
  
  private var _density: Float = 1f
  private var _scaledDensity: Float = 1f
  
  val density: Float get() = _density
  val scaledDensity: Float get() = _scaledDensity
  
  fun init(resources: Resources) {
    _density = resources.displayMetrics.density
    _scaledDensity = resources.displayMetrics.scaledDensity
  }
}