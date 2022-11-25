package navigation

class NavigationController private constructor(
  private val router: BaseRouter
) {
  
  fun getNavigatorHolder(): NavigatorHolder = router.commandBuffer
  
  companion object {
    
    /**
     * Creates the [NavigationController] instance with the custom router.
     * @param customRouter the custom router extending [BaseRouter]
     */
    @JvmStatic
    fun <T : BaseRouter> create(customRouter: T) = NavigationController(customRouter)
  }
}
