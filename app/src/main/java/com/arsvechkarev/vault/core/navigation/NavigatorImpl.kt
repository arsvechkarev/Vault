package com.arsvechkarev.vault.core.navigation

import com.arsvechkarev.vault.core.navigation.AnimationType.ANIMATION_BACKWARD
import com.arsvechkarev.vault.core.navigation.AnimationType.ANIMATION_FORWARD
import com.arsvechkarev.vault.core.navigation.AnimationType.ANIMATION_NONE
import kotlin.reflect.KClass

class NavigatorImpl(
  private val host: NavigationHost,
  private val screenHandlerFactory: ScreenHandlerFactory,
) : Navigator {
  
  private val screenHandlersCache = HashMap<String, ScreenHandler>()
  private val screensStack = ArrayList<String>()
  
  override fun goTo(screenClass: KClass<out Screen>, options: ForwardOptions) {
    if (screensStack.lastOrNull() == screenClass.java.name) {
      return
    }
    val screenHandler = getOrCreateScreenHandler(screenClass.java.name)
    screenHandler.setupArguments(options.arguments)
    val isOperationReplace = options.replaceCurrentScreen
    screensStack.lastOrNull()?.let { screenClassName ->
      hideScreen(screenClassName, ANIMATION_FORWARD, releaseAfterwards = isOperationReplace)
    }
    if (isOperationReplace) {
      screensStack.removeLast()
    }
    screensStack.add(screenClass.java.name)
    showScreen(screenClass.java.name, ANIMATION_FORWARD)
  }
  
  override fun goBack(options: BackwardOptions) {
    assertThat(screensStack.isNotEmpty())
    val screenOnTop = screensStack.removeLast()
    val screenHandlerOnTop = getOrCreateScreenHandler(screenOnTop)
    if (screensStack.isEmpty()) {
      screenHandlerOnTop.performHide(ANIMATION_NONE)
      screenHandlerOnTop.performRelease()
      host.removeAllScreenHandlers()
      screenHandlersCache.clear()
    } else {
      hideScreen(screenOnTop, ANIMATION_BACKWARD, releaseAfterwards = options.removeCurrentScreen)
      val currentScreen = screensStack.last()
      showScreen(currentScreen, ANIMATION_BACKWARD)
    }
  }
  
  override fun goBackTo(screenClass: KClass<out Screen>, options: BackwardToOptions) {
    assertThat(screensStack.size >= 2)
    val newScreenIndex = screensStack.indexOf(screenClass.java.name)
    assertThat(newScreenIndex != -1)
    val currentScreen = screensStack.removeLast()
    val newScreensList = ArrayList(screensStack.subList(0, newScreenIndex + 1).toMutableList())
    val screensToDelete = ArrayList(screensStack.subList(newScreenIndex + 1, screensStack.size))
    screensStack.clear()
    screensStack.addAll(newScreensList)
    hideScreen(currentScreen, ANIMATION_BACKWARD, options.removeAllLeftScreens)
    showScreen(screenClass.java.name, ANIMATION_BACKWARD)
    releaseScreens(screensToDelete, options.removeAllLeftScreens)
  }
  
  override fun handleGoBack(): Boolean {
    if (screensStack.isEmpty()) return true
    val screenOnTop = screensStack.last()
    val screenHandlerOnTop = screenHandlersCache.getValue(screenOnTop)
    if (!screenHandlerOnTop.allowBackPress()) {
      return true
    }
    screensStack.removeLast()
    if (screensStack.isEmpty()) {
      screenHandlerOnTop.performRelease()
      return false
    }
    hideScreen(screenOnTop, ANIMATION_BACKWARD, releaseAfterwards = false)
    showScreen(screensStack.last(), ANIMATION_BACKWARD)
    return true
  }
  
  override fun switchToNewRoot(screenClass: KClass<out Screen>) {
    screensStack.lastOrNull()?.let { screenClassName ->
      hideScreen(screenClassName, ANIMATION_FORWARD, releaseAfterwards = true)
    }
    screenHandlersCache.remove(screenClass.java.name)
    screenHandlersCache.values.forEach { handler ->
      handler.performHide(ANIMATION_NONE)
      handler.performRelease()
    }
    screenHandlersCache.clear()
    screensStack.clear()
    host.removeAllScreenHandlers()
    goTo(screenClass)
  }
  
  val stack get() = ArrayList(screensStack)
  
  val screensCache get() = HashMap(screenHandlersCache)
  
  private fun getOrCreateScreenHandler(
    screenClassKey: String
  ) = screenHandlersCache[screenClassKey] ?: run {
    val screenInstance = Class.forName(screenClassKey).newInstance() as Screen
    val screenHandler = screenHandlerFactory.createScreenHandler(screenInstance)
    screenHandlersCache[screenClassKey] = screenHandler
    screenHandler
  }
  
  private fun hideScreen(
    screenClassName: String,
    animationType: AnimationType,
    releaseAfterwards: Boolean
  ) {
    val screenHandler = screenHandlersCache[screenClassName]
    screenHandler?.performHide(animationType)
    if (releaseAfterwards && screenHandler != null) {
      screenHandler.performRelease()
      host.removeScreenHandlerAfterDelay(screenHandler, screenHandler.animationDelay,
        rightBeforeRemoving = { screenHandler.performRelease() })
      screenHandlersCache.remove(screenClassName)
    }
  }
  
  private fun showScreen(screenClassName: String, animationType: AnimationType) {
    val screenHandler = getOrCreateScreenHandler(screenClassName)
    screenHandler.buildViewIfNeeded()
    if (!host.isAttached(screenHandler)) {
      host.addScreenHandler(screenHandler)
      screenHandler.performInit()
    }
    screenHandler.performShow(animationType)
  }
  
  private fun releaseScreens(screensToDelete: ArrayList<String>, removeFromHost: Boolean) {
    screensToDelete.forEach { screen ->
      screenHandlersCache[screen]?.let { handler ->
        handler.performHide(ANIMATION_NONE)
        if (removeFromHost) {
          host.removeScreenHandler(handler)
          screenHandlersCache.remove(screen)
        }
      }
    }
  }
}