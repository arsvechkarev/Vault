package navigation

fun interface ScreenFactory {
  
  fun createScreen(screenKey: ScreenKey): Screen
}

object OfClassNameFactory : ScreenFactory {
  
  override fun createScreen(screenKey: ScreenKey): Screen {
    
    return Class.forName(screenKey.screenClassName).newInstance() as Screen
  }
}