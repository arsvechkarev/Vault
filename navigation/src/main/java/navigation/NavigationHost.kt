package navigation

/**
 * Host for displaying screens
 */
interface NavigationHost {
  
  /**
   * Returns true, if view of this [handler] is attached to screen, false otherwise
   */
  fun isAttached(handler: ScreenHandler): Boolean
  
  /**
   * Add [handler] to screens
   */
  fun addScreenHandler(handler: ScreenHandler)
  
  /**
   * Removes [handler] from screens
   */
  fun removeScreenHandler(handler: ScreenHandler)
  
  /**
   * Removes [handler] from screens after [delay]
   */
  fun removeScreenHandlerAfterDelay(handler: ScreenHandler, delay: Long, afterDelay: () -> Unit)
  
  /**
   * Removes all screens from host
   */
  fun removeAllScreenHandlers()
}