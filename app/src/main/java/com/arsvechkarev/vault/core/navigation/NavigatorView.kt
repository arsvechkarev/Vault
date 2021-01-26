package com.arsvechkarev.vault.core.navigation

import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.WindowInsets
import android.widget.FrameLayout
import com.arsvechkarev.core.navigation.Options
import com.arsvechkarev.vault.core.extensions.ifNotNull
import com.arsvechkarev.vault.viewdsl.DURATION_SHORT
import com.arsvechkarev.vault.viewdsl.animateGone
import com.arsvechkarev.vault.viewdsl.animateSlideFromRight
import com.arsvechkarev.vault.viewdsl.animateSlideToRight
import com.arsvechkarev.vault.viewdsl.animateVisible
import com.arsvechkarev.vault.viewdsl.invisible
import timber.log.Timber
import kotlin.reflect.KClass

class NavigatorView(context: Context) : FrameLayout(context) {
  
  private val screenClassesToScreens = HashMap<String, Screen>()
  private val screens = ArrayList<Screen>()
  private var _currentScreen: Screen? = null
  
  init {
    fitsSystemWindows = true
  }
  
  /**
   * Navigates to screen [screenClass] with given [options]
   */
  fun navigate(screenClass: KClass<out Screen>, options: Options = Options()) {
    if (_currentScreen != null && _currentScreen!!::class.java.name == screenClass.java.name) {
      return
    }
    if (options.clearAllOtherScreens) {
      screenClassesToScreens.values.forEach { releaseScreen(it) }
      screenClassesToScreens.clear()
      screens.clear()
      removeAllViews()
    }
    val screen = getOrCreateScreen(screenClass, options)
    val view = getOrBuildScreenView(screen)
    view.invisible() // Make view invisible so that we can animate it later
    _currentScreen.ifNotNull {
      hideScreen(it, animateSlideToRight = false)
      if (options.removeCurrentScreen) {
        scheduleScreenRemoving(it)
      }
    }
    screens.add(screen)
    if (view.isAttachedToWindow) {
      showScreen(screen, animateSlideFromRight = true)
    } else {
      addView(view, MATCH_PARENT, MATCH_PARENT)
      screen.onInitDelegate()
      if (screen.metadata._arguments == null) {
        screen.onInit()
      } else {
        screen.onInit(screen.metadata._arguments!!)
      }
      showScreen(screen, animateSlideFromRight = true)
    }
    _currentScreen = screen
  }
  
  /**
   * Returns true if there are screens in the stack or screen handled back press
   */
  fun handleGoBack(): Boolean {
    if (screens.isEmpty()) return false
    val lastScreen = screens.last()
    if (!lastScreen.allowBackPress()) {
      return true
    }
    if (screens.size == 1) {
      releaseScreen(_currentScreen!!)
      screenClassesToScreens.clear()
      screens.clear()
      _currentScreen = null
      return false
    }
    hideScreen(lastScreen, animateSlideToRight = true)
    if (lastScreen.metadata.removeOnExit) {
      lastScreen.view.ifNotNull { view ->
        view.apply(screenHidingAnimation)
        postDelayed({ performRemoveView(view) }, (ANIMATION_DURATION * 1.2f).toLong())
      }
    }
    screens.removeLast()
    _currentScreen = screens.last()
    showScreen(_currentScreen!!, animateSlideFromRight = false)
    return true
  }
  
  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    _currentScreen.ifNotNull { checkForOrientation(it, newConfig) }
  }
  
  override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
    return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0,
      insets.systemWindowInsetBottom))
  }
  
  private fun checkForOrientation(screen: Screen, config: Configuration) {
    if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
      screen.onOrientationBecamePortrait()
    } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      screen.onOrientationBecameLandscape()
    }
  }
  
  private fun getOrCreateScreen(
    screenClass: KClass<out Screen>,
    options: Options
  ) = screenClassesToScreens[screenClass.java.name] ?: run {
    val constructor = screenClass.java.getConstructor()
    val instance = constructor.newInstance()
    val className = instance::class.java.name
    screenClassesToScreens[className] = instance
    instance.metadata._context = context
    instance.metadata._arguments = options.arguments
    instance.metadata.removeOnExit = options.removeOnExit
    instance
  }
  
  private fun getOrBuildScreenView(screen: Screen): View {
    return screen.view ?: run {
      val builtView = screen.buildLayout()
      screen.metadata._view = builtView
      builtView.tag = screen::class.java.name
      builtView
    }
  }
  
  private fun showScreen(screen: Screen, animateSlideFromRight: Boolean) {
    if (animateSlideFromRight) {
      screen.view?.apply(newScreenAppearanceAnimation)
    } else {
      screen.view?.apply(screenReappearanceAnimation)
    }
    screen.onAppearedOnScreenDelegate()
    screen.onAppearedOnScreen()
    checkForOrientation(screen, context.resources.configuration)
  }
  
  private fun hideScreen(screen: Screen, animateSlideToRight: Boolean) {
    screen.onRelease()
    screen.onDetachDelegate()
    if (animateSlideToRight) {
      screen.view?.apply(screenHidingAnimation)
    } else {
      screen.view?.apply(oldScreenHidingAnimation)
    }
  }
  
  private fun scheduleScreenRemoving(currentScreen: Screen) {
    screenClassesToScreens.remove(currentScreen.javaClass.name)
    screens.remove(currentScreen)
    postDelayed({
      removeView(currentScreen.view)
      releaseScreen(currentScreen)
      dump()
    }, ANIMATION_DURATION)
  }
  
  private fun performRemoveView(child: View) {
    removeView(child)
    val screen = screenClassesToScreens.getValue(child.tag as String)
    releaseScreen(screen)
    screens.remove(screen)
    screenClassesToScreens.remove(screen.javaClass.name)
  }
  
  private fun releaseScreen(screen: Screen) {
    screen.onRelease()
    screen.metadata._context = null
    screen.metadata._view = null
    screen.onDestroyDelegate()
  }
  
  private fun dump() {
    val listToString = { list: List<Screen> ->
      buildString {
        append("[")
        list.forEach {
          append(it.javaClass.simpleName)
          append(", ")
        }
        delete(length - 2, length)
        append("]")
      }
    }
    Timber.d("Current screen = $_currentScreen")
    Timber.d("Current screens size = ${screens.size}")
    Timber.d("Screens list = ${listToString(screens)}")
    Timber.d("Cached list size = ${screenClassesToScreens.size}")
    Timber.d("Cached list = $screenClassesToScreens")
  }
  
  companion object {
    
    private const val ANIMATION_DURATION = DURATION_SHORT
    
    private val newScreenAppearanceAnimation: View.() -> Unit = {
      animateSlideFromRight(duration = ANIMATION_DURATION)
    }
    
    private val screenReappearanceAnimation: View.() -> Unit = {
      animateVisible(duration = ANIMATION_DURATION)
    }
    
    private val screenHidingAnimation: View.() -> Unit = {
      animateSlideToRight(duration = ANIMATION_DURATION)
    }
    
    private val oldScreenHidingAnimation: View.() -> Unit = {
      animateGone(duration = ANIMATION_DURATION)
    }
  }
}