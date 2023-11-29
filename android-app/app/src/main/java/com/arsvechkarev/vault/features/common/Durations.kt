package com.arsvechkarev.vault.features.common

import com.arsvechkarev.vault.features.common.di.CoreComponentHolder

/**
 * Represents duration values for animations
 */
object Durations {
  
  const val Short = 170L
  const val Default = 300L
  const val StubDelay = 600L
  const val Checkmark = 1300L
  const val Snackbar = 1000L
  const val MenuOpening = 300L
  const val Shimmer = 1100L
  
  val DelayOpenKeyboard
    get() = CoreComponentHolder.coreComponent.application.resources
        .getInteger(navigation.R.integer.navigation_animation_duration).toLong()
}
