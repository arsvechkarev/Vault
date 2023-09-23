package com.arsvechkarev.vault.features.common

import navigation.Back
import navigation.BaseRouter
import navigation.Forward
import navigation.ForwardWithRemovalOf
import navigation.ScreenInfo
import navigation.SwitchToNewRoot

class Router : BaseRouter() {
  
  fun goForward(screenInfo: ScreenInfo, animate: Boolean = true) {
    executeCommands(Forward(screenInfo, animate))
  }
  
  fun goForwardWithRemovalOf(screenInfo: ScreenInfo, screensCount: Int) {
    executeCommands(ForwardWithRemovalOf(screenInfo, screensCount))
  }
  
  fun goBack() {
    executeCommands(Back)
  }
  
  fun switchToNewRoot(screenInfo: ScreenInfo) {
    executeCommands(SwitchToNewRoot(screenInfo))
  }
}
