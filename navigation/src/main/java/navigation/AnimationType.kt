package navigation

/**
 * Types of animation for screen
 */
enum class AnimationType {
  
  /**
   * No animation. Just set screen to visible/invisible
   */
  ANIMATION_NONE,
  
  /**
   * Animating screen as going forward
   */
  ANIMATION_FORWARD,
  
  /**
   * Animating screen as going backward
   */
  ANIMATION_BACKWARD
}