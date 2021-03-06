package com.arsvechkarev.vault.core.navigation

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
   * Add [handler] to screens
   */
  fun removeScreenHandler(handler: ScreenHandler)
  
  /**
   * Removes [handler] after [animationDelay], executing [rightBeforeRemoving] before finally
   * removing this handler from host
   */
  fun removeScreenHandlerAfterDelay(handler: ScreenHandler,
                                    animationDelay: Long,
                                    rightBeforeRemoving: () -> Unit = {})
  
  /**
   * Removes all screens from host
   */
  fun removeAllScreenHandlers()
}