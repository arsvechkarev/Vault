package com.arsvechkarev.vault.core.navigation

import android.content.Context
import android.view.View
import com.arsvechkarev.vault.core.navigation.AnimationType.ANIMATION_BACKWARD
import com.arsvechkarev.vault.core.navigation.AnimationType.ANIMATION_FORWARD
import com.arsvechkarev.vault.core.navigation.AnimationType.ANIMATION_NONE
import com.arsvechkarev.vault.viewdsl.animateGone
import com.arsvechkarev.vault.viewdsl.animateSlideFromRight
import com.arsvechkarev.vault.viewdsl.animateSlideToRight
import com.arsvechkarev.vault.viewdsl.animateVisible
import com.arsvechkarev.vault.viewdsl.gone
import com.arsvechkarev.vault.viewdsl.visible

class ViewScreenHandler(
  private val screen: ViewScreen,
  private val context: Context
) : ScreenHandler {
  
  private var _arguments: Map<String, Any> = HashMap()
  
  val view: View get() = screen.viewNonNull
  
  override val animationDelay = ANIMATION_DURATION
  
  override fun setupArguments(arguments: Map<String, Any>) {
    _arguments = arguments
  }
  
  override fun buildViewIfNeeded() {
    screen._context = context
    if (screen._view == null) {
      screen._view = screen.buildLayout()
    }
  }
  
  override fun performInit() {
    screen.onInit(_arguments)
  }
  
  override fun performShow(type: AnimationType) {
    when (type) {
      ANIMATION_NONE -> view.visible()
      ANIMATION_FORWARD -> view.apply(appearanceAsGoingForward)
      ANIMATION_BACKWARD -> view.apply(appearanceAsGoingBackward)
    }
    screen.onAppearedOnScreen(_arguments)
  }
  
  override fun performHide(type: AnimationType) {
    when (type) {
      ANIMATION_NONE -> view.gone()
      ANIMATION_FORWARD -> view.apply(disappearanceAsGoingForward)
      ANIMATION_BACKWARD -> view.apply(disappearanceAsGoingBackward)
    }
    screen.onDisappearedFromScreen()
  }
  
  override fun performRelease() {
    screen._context = null
  }
  
  override fun allowBackPress(): Boolean {
    return screen.allowBackPress()
  }
  
  override fun onOrientationBecamePortrait() {
    screen.onOrientationBecamePortrait()
  }
  
  override fun onOrientationBecameLandscape() {
    screen.onOrientationBecameLandscape()
  }
  
  private companion object {
    
    const val ANIMATION_DURATION = 150L
    
    private val appearanceAsGoingForward: View.() -> Unit = {
      animateSlideFromRight(duration = ANIMATION_DURATION)
    }
    
    private val appearanceAsGoingBackward: View.() -> Unit = {
      animateVisible(duration = ANIMATION_DURATION)
    }
    
    private val disappearanceAsGoingForward: View.() -> Unit = {
      animateGone(duration = ANIMATION_DURATION)
    }
    
    private val disappearanceAsGoingBackward: View.() -> Unit = {
      animateSlideToRight(duration = ANIMATION_DURATION)
    }
  }
}