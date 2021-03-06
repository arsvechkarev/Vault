package com.arsvechkarev.vault.core.navigation

interface ScreenHandlerFactory {
  
  /**
   * Creates handler for a [screen]
   */
  fun createScreenHandler(screen: Screen): ScreenHandler
}
