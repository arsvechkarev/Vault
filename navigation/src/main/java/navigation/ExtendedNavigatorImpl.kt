package navigation

import android.os.Bundle
import android.util.Log
import androidx.annotation.VisibleForTesting
import com.github.terrakok.cicerone.Command
import navigation.AnimationType.ANIMATION_FORWARD
import navigation.AnimationType.ANIMATION_NONE

class ExtendedNavigatorImpl(
  private val host: NavigationHost,
  private val screenFactory: ScreenFactory,
  private val screenHandlerFactory: ScreenHandlerFactory,
) : ExtendedNavigator {
  
  // Map of screen keys to handlers
  private val screenHandlersCache = HashMap<ScreenKey, ScreenHandler>()
  
  // Stack of screen keys
  private val screensStack = ArrayList<ScreenKey>()
  
  // Copy of screens for tests
  @Suppress("PropertyName")
  @VisibleForTesting
  internal val _test_screensStack
    get() = ArrayList(screensStack)
  
  // Copy of screen handlers for tests
  @Suppress("PropertyName")
  @VisibleForTesting
  internal val _test_screenHandlersCache
    get() = HashMap(screenHandlersCache)
  
  override fun applyCommands(commands: Array<out Command>) {
    for (command in commands) {
      when (command) {
        is Forward -> performForward(command.screenInfo.screenKey,
          command.screenInfo.arguments, command.clearContainer)
        is Replace -> performReplace(command.screenInfo.screenKey,
          command.screenInfo.arguments)
        is Back -> performBack(command.releaseCurrentScreen)
        is BackTo -> performBackTo(command.screenInfo.screenKey,
          command.releaseAllLeftScreens)
        is SwitchToNewRoot -> performSwitchToNewRoot(command.screenInfo.screenKey,
          command.screenInfo.arguments)
      }
    }
    dump()
  }
  
  override fun handleGoBack(): Boolean {
    if (screensStack.isEmpty()) return true
    val screenOnTop = screensStack.last()
    val screenHandlerOnTop = screenHandlersCache.getValue(screenOnTop)
    if (screenHandlerOnTop.handleBackPress()) {
      return true
    }
    screensStack.removeLast()
    if (screensStack.isEmpty()) {
      screenHandlerOnTop.performFullRelease()
      return false
    }
    hideScreen(screenOnTop, AnimationType.ANIMATION_BACKWARD, releaseAfterwards = true)
    showScreen(screensStack.last(), AnimationType.ANIMATION_BACKWARD)
    dump()
    return true
  }
  
  override fun onSaveInstanceState(bundle: Bundle) {
    val stack = getStackAsArrayList()
    bundle.putStringArrayList(BUNDLE_KEY, stack)
    screenHandlersCache.forEach { (_, value) -> value.performSaveInstanceState(bundle) }
  }
  
  override fun onRestoreInstanceState(bundle: Bundle?) {
    if (bundle == null) return
    screensStack.clear()
    val stringArrayList = bundle.getStringArrayList(BUNDLE_KEY)
    screensStack.addAll(getScreensFromStringsList(stringArrayList!!))
    screensStack.lastOrNull()?.let { screenOnTop ->
      showScreen(screenOnTop, ANIMATION_NONE)
    }
  }
  
  override fun releaseScreens() {
    screenHandlersCache.forEach { (_, screenHandler) ->
      screenHandler.performOnlyViewRemoval()
    }
  }
  
  private fun performForward(screenKey: ScreenKey, arguments: Bundle, removeCurrentScreen: Boolean) {
    if (screensStack.lastOrNull() == screenKey) {
      // Current screen is already in front of the user, return
      return
    }
    val screenHandler = getOrCreateScreenHandler(screenKey)
    screenHandler.setupArguments(arguments)
    screensStack.lastOrNull()?.let { currentScreenKey ->
      if (removeCurrentScreen) {
        removeScreenView(currentScreenKey)
      } else {
        hideScreen(currentScreenKey, ANIMATION_FORWARD, releaseAfterwards = false)
      }
    }
    screensStack.add(screenKey)
    // If stack has only one screen (one that was just added to stack), then showing this
    // screen without fancy animation
    val animationType = if (screensStack.size == 1) ANIMATION_NONE else ANIMATION_FORWARD
    showScreen(screenKey, animationType)
  }
  
  private fun performReplace(screenKey: ScreenKey, arguments: Bundle) {
    if (screensStack.lastOrNull() == screenKey) {
      // Current screen is already in front of the user, return
      return
    }
    val screenHandler = getOrCreateScreenHandler(screenKey)
    screenHandler.setupArguments(arguments)
    screensStack.lastOrNull()?.let { screenClassName ->
      hideScreen(screenClassName, ANIMATION_FORWARD,
        releaseAfterwards = true)
    }
    screensStack.removeLast()
    screensStack.add(screenKey)
    showScreen(screenKey, ANIMATION_FORWARD)
  }
  
  private fun performBack(releaseCurrentScreen: Boolean) {
    require(screensStack.isNotEmpty())
    val screenOnTop = screensStack.removeLast()
    val screenHandlerOnTop = getOrCreateScreenHandler(screenOnTop)
    if (screensStack.isEmpty()) {
      screenHandlerOnTop.performHide(ANIMATION_NONE)
      screenHandlerOnTop.performFullRelease()
      host.removeAllScreenHandlers()
      screenHandlersCache.clear()
    } else {
      hideScreen(screenOnTop, AnimationType.ANIMATION_BACKWARD,
        releaseAfterwards = releaseCurrentScreen)
      val currentScreen = screensStack.last()
      showScreen(currentScreen, AnimationType.ANIMATION_BACKWARD)
    }
  }
  
  private fun performBackTo(screenKey: ScreenKey, releaseAllLeftScreens: Boolean) {
    require(screensStack.size >= 2)
    val newScreenIndex = screensStack.indexOf(screenKey)
    require(newScreenIndex != -1)
    val currentScreen = screensStack.removeLast()
    val newScreensList = ArrayList(screensStack.subList(0, newScreenIndex + 1).toMutableList())
    val screensToDelete = ArrayList(screensStack.subList(newScreenIndex + 1, screensStack.size))
    screensStack.clear()
    screensStack.addAll(newScreensList)
    hideScreen(currentScreen, AnimationType.ANIMATION_BACKWARD, releaseAllLeftScreens)
    showScreen(screenKey, AnimationType.ANIMATION_BACKWARD)
    releaseScreens(screensToDelete, removeFromHost = releaseAllLeftScreens)
  }
  
  private fun performSwitchToNewRoot(screenKey: ScreenKey, arguments: Bundle) {
    screensStack.lastOrNull()?.let { currentScreenKey ->
      hideScreen(currentScreenKey, ANIMATION_FORWARD, releaseAfterwards = true)
    }
    screenHandlersCache.remove(screenKey)
    screenHandlersCache.values.forEach { screenHandler ->
      if (host.isAttached(screenHandler)) host.removeScreenHandler(screenHandler)
      screenHandler.performFullRelease()
    }
    screenHandlersCache.clear()
    screensStack.clear()
    val screenHandler = getOrCreateScreenHandler(screenKey)
    screenHandler.setupArguments(arguments)
    screensStack.add(screenKey)
    showScreen(screenKey, ANIMATION_FORWARD)
  }
  
  private fun getOrCreateScreenHandler(
    screenKey: ScreenKey
  ) = screenHandlersCache[screenKey] ?: run {
    val screenInstance = screenFactory.createScreen(screenKey)
    val screenHandler = screenHandlerFactory.createScreenHandler(screenKey, screenInstance)
    screenHandlersCache[screenKey] = screenHandler
    screenHandler
  }
  
  private fun removeScreenView(screenKey: ScreenKey) {
    val screenHandler = screenHandlersCache.getValue(screenKey)
    screenHandler.performHide(ANIMATION_FORWARD)
    host.removeScreenHandlerAfterDelay(screenHandler, screenHandler.hideAnimationDuration,
      afterDelay = { screenHandler.performOnlyViewRemoval() })
  }
  
  private fun hideScreen(
    screenKey: ScreenKey,
    animationType: AnimationType,
    releaseAfterwards: Boolean
  ) {
    val screenHandler = screenHandlersCache[screenKey]
    screenHandler?.performHide(animationType)
    if (releaseAfterwards && screenHandler != null) {
      host.removeScreenHandlerAfterDelay(screenHandler, screenHandler.hideAnimationDuration,
        afterDelay = { screenHandler.performFullRelease() })
      screenHandlersCache.remove(screenKey)
    }
  }
  
  private fun showScreen(screenKey: ScreenKey, animationType: AnimationType) {
    val screenHandler = getOrCreateScreenHandler(screenKey)
    screenHandler.buildViewIfNeeded()
    if (!host.isAttached(screenHandler)) {
      host.addScreenHandler(screenHandler)
      screenHandler.performInit()
    }
    screenHandler.performShow(animationType)
  }
  
  private fun releaseScreens(screensToDelete: ArrayList<ScreenKey>, removeFromHost: Boolean) {
    screensToDelete.forEach { screen ->
      screenHandlersCache[screen]?.let { handler ->
        if (removeFromHost) {
          handler.performFullRelease()
          host.removeScreenHandler(handler)
          screenHandlersCache.remove(screen)
        }
      }
    }
  }
  
  private fun getStackAsArrayList(): ArrayList<String> {
    val dst = ArrayList<String>(_test_screensStack.size)
    return screensStack.mapTo(dst, ScreenKey::toString)
  }
  
  private fun getScreensFromStringsList(list: ArrayList<String>): List<ScreenKey> {
    return list.map(ScreenKey::fromString)
  }
  
  private fun dump() {
    val stack = buildString {
      append("Screens stack:\n")
      screensStack.forEachIndexed { i, screenKey ->
        append("\r$i -> $screenKey\n")
      }
    }
    val cache = buildString {
      append("Screens in cache:\n")
      screenHandlersCache.forEach { (screenKey, _) ->
        append("\r").append("$screenKey\n")
      }
    }
    Log.d("ExtendedNavigatorImpl", "=====================")
    Log.d("ExtendedNavigatorImpl", stack)
    Log.d("ExtendedNavigatorImpl", "")
    Log.d("ExtendedNavigatorImpl", cache)
    Log.d("ExtendedNavigatorImpl", "=====================")
  }
  
  companion object {
    
    private const val BUNDLE_KEY = "ExtendedNavigatorImpl:ScreenKeys"
  }
}