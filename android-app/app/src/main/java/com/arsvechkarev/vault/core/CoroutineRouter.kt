package com.arsvechkarev.vault.core

import com.github.terrakok.cicerone.BaseRouter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import navigation.Back
import navigation.BackTo
import navigation.Forward
import navigation.Replace
import navigation.ScreenInfo
import navigation.SwitchToNewRoot

class CoroutineRouter(private val scope: CoroutineScope) : BaseRouter() {
  
  fun goForward(screenInfo: ScreenInfo, removeCurrentView: Boolean = false) {
    scope.launch { executeCommands(Forward(screenInfo, removeCurrentView)) }
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