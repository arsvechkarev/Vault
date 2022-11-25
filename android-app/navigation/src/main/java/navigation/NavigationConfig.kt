package navigation

object NavigationConfig {
  
  internal var animations: NavigationAnimations = DefaultNavigationAnimations
    private set
  
  fun setAnimations(animations: NavigationAnimations) {
    this.animations = animations
  }
}
