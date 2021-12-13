package navigation.fakes

import navigation.Screen
import navigation.ScreenHandler
import navigation.ScreenHandlerFactory
import navigation.ScreenKey

object FakeScreenHandlerFactory : ScreenHandlerFactory {
  
  override fun createScreenHandler(screenKey: ScreenKey, screen: Screen): ScreenHandler {
    return FakeScreenHandler(screen as FakeViewHavingScreen)
  }
}