package com.arsvechkarev.vault.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import navigation.Back
import navigation.BackTo
import navigation.BaseRouter
import navigation.Forward
import navigation.Replace
import navigation.ScreenInfo
import navigation.SwitchToNewRoot

class Router(private val scope: CoroutineScope) : BaseRouter() {
  
  fun goForward(screenInfo: ScreenInfo, removeCurrentView: Boolean = false) {
    scope.launch { executeCommands(Forward(screenInfo, removeCurrentView)) }
  }
  
  fun goForwardWithDelay(screenInfo: ScreenInfo, delay: Long, removeCurrentView: Boolean = false) {
    scope.launch {
      delay(delay)
      executeCommands(Forward(screenInfo, removeCurrentView))
    }
  }
  
  fun replace(screenInfo: ScreenInfo) {
    scope.launch { executeCommands(Replace(screenInfo)) }
  }
  
  fun goBack(releaseCurrentScreen: Boolean = true) {
    scope.launch { executeCommands(Back(releaseCurrentScreen)) }
  }
  
  fun goBackTo(screenInfo: ScreenInfo, releaseAllLeftScreens: Boolean = true) {
    scope.launch { executeCommands(BackTo(screenInfo, releaseAllLeftScreens)) }
  }
  
  fun switchToNewRoot(screenInfo: ScreenInfo) {
    scope.launch { executeCommands(SwitchToNewRoot(screenInfo)) }
  }
}