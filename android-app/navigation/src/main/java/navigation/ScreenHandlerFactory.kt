package navigation

fun interface ScreenHandlerFactory {
  
  /**
   * Creates handler for a [screen] and [screenKey]
   */
  fun createScreenHandler(screenKey: ScreenKey, screen: Screen): ScreenHandler
}
