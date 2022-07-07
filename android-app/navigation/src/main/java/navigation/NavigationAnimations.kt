package navigation

import android.view.View

interface NavigationAnimations {
  
  fun AppearanceAsGoingForward(doOnEnd: () -> Unit): View.() -> Unit
  
  fun AppearanceAsGoingBackward(doOnEnd: () -> Unit): View.() -> Unit
  
  fun DisappearanceAsGoingForward(doOnEnd: () -> Unit): View.() -> Unit
  
  fun DisappearanceAsGoingBackward(doOnEnd: () -> Unit): View.() -> Unit
}

object DefaultNavigationAnimations : NavigationAnimations {
  
  override fun AppearanceAsGoingForward(doOnEnd: () -> Unit): View.() -> Unit = {
    visibility = View.VISIBLE
  }
  
  override fun AppearanceAsGoingBackward(doOnEnd: () -> Unit): View.() -> Unit = {
    visibility = View.VISIBLE
  }
  
  override fun DisappearanceAsGoingForward(doOnEnd: () -> Unit): View.() -> Unit = {
    visibility = View.GONE
  }
  
  override fun DisappearanceAsGoingBackward(doOnEnd: () -> Unit): View.() -> Unit = {
    visibility = View.GONE
  }
}
