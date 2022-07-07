package navigation

/**
 * Types of animation for screen
 */
enum class AnimationType {
  
  /**
   * No animation. Just set screen to visible/invisible
   */
  NONE,
  
  /**
   * Animating screen as going forward
   */
  FORWARD,
  
  /**
   * Animating screen as going backward
   */
  BACKWARD
}