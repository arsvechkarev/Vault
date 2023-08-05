package navigation

interface Navigator {
  
  /**
   * Performs transition described by the navigation command
   *
   * @param commands the navigation command array to apply per single transaction
   */
  fun applyCommands(commands: Array<out Command>)
  
  /**
   * Handles going back in stack. Returns true, if there were any screens in stack to be removed
   * (and removes screen), or if current screen handled back interaction. Returns false otherwise
   */
  fun handleGoBack(): Boolean
}
