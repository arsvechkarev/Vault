package com.arsvechkarev.vault.core

import android.view.View
import config.DurationsConfigurator
import navigation.NavigationAnimations
import navigation.animateGoneAfter
import navigation.animateSlideFromRight
import navigation.animateSlideToRight
import navigation.animateVisible

object VaultNavigationAnimations : NavigationAnimations {
  
  override fun AppearanceAsGoingForward(doOnEnd: () -> Unit): View.() -> Unit = {
    animateSlideFromRight(duration = DurationsConfigurator.Short, doOnEnd)
  }
  
  override fun AppearanceAsGoingBackward(doOnEnd: () -> Unit): View.() -> Unit = {
    animateVisible(duration = DurationsConfigurator.Short, doOnEnd)
  }
  
  override fun DisappearanceAsGoingForward(doOnEnd: () -> Unit): View.() -> Unit = {
    animateGoneAfter(duration = DurationsConfigurator.Short, doOnEnd)
  }
  
  override fun DisappearanceAsGoingBackward(doOnEnd: () -> Unit): View.() -> Unit = {
    animateSlideToRight(duration = DurationsConfigurator.Short, doOnEnd)
  }
}