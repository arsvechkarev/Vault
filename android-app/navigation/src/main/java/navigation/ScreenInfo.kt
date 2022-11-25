package navigation

import android.os.Bundle
import kotlin.reflect.KClass

/**
 * Creates [ScreenInfo]
 *
 * @param screenClass Class for screen creation. Make sure this screen has empty no-args constructor
 * @param arguments for screen
 * @param additionalTag Additional Tag to be added to screen (Use it to distinguish two
 * different screens with the same class name)
 */
fun Screen(
  arguments: Bundle = Bundle.EMPTY,
  additionalTag: String = "",
  screenClass: () -> KClass<out BaseFragmentScreen>,
): ScreenInfo {
  return ScreenInfo(ScreenKey(screenClass().java.name, additionalTag), arguments)
}

/**
 * Screen information holder that has [screenKey] and [arguments] for screen
 */
data class ScreenInfo(
  val screenKey: ScreenKey,
  val arguments: Bundle,
)

/**
 * Key that helps to convert [screenClassName] and [additionalTag] to and from string
 */
data class ScreenKey(val screenClassName: String, val additionalTag: String = "") {
  
  override fun toString(): String {
    return screenClassName + DELIMITER + additionalTag
  }
  
  companion object {
    
    private const val DELIMITER = ":"
    
    fun fromString(string: String): ScreenKey {
      return ScreenKey(string.substringBefore(DELIMITER), string.substringAfter(DELIMITER))
    }
  }
}