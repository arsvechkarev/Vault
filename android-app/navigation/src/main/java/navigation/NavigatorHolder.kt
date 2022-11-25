package navigation

/**
 * Navigator holder interface.
 *
 * Use it to connect a [Navigator] to the [NavigationController].
 */
interface NavigatorHolder {
  /**
   * Set an active Navigator for the [NavigationController] and start receive commands.
   *
   * @param navigator new active Navigator
   */
  fun setNavigator(navigator: Navigator)
  
  /**
   * Remove the current Navigator and stop receive commands.
   */
  fun removeNavigator()
}
