package navigation

import navigation.fakes.FakeNavigationHost
import navigation.fakes.FakeScreenHandler
import navigation.fakes.FakeScreenHandlerFactory
import navigation.fakes.FakeViewHavingScreen
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ExtendedNavigatorTest {
  
  class Screen1 : FakeViewHavingScreen()
  class Screen2 : FakeViewHavingScreen()
  class Screen3 : FakeViewHavingScreen()
  class Screen4 : FakeViewHavingScreen()
  class Screen5 : FakeViewHavingScreen()
  
  private val screen1 = Screen { Screen1::class }
  private val screen2 = Screen { Screen2::class }
  private val screen3 = Screen { Screen3::class }
  private val screen4 = Screen { Screen4::class }
  private val screen5 = Screen { Screen5::class }
  
  lateinit var host: FakeNavigationHost
  lateinit var navigator: ExtendedNavigatorImpl
  
  @Before
  fun setUp() {
    host = FakeNavigationHost { handler -> (handler as FakeScreenHandler).fakeView!! }
    navigator = ExtendedNavigatorImpl(host, OfClassNameFactory, FakeScreenHandlerFactory)
  }
  
  @Test
  fun `Basic opening`() {
    assertTrue(host.views.isEmpty())
    assertTrue(navigator._test_screensStack.isEmpty())
    assertTrue(navigator._test_screenHandlersCache.isEmpty())
    
    navigator.applyCommand(Forward(screen1))
    
    with(host) {
      hasViewCount(1)
      hasViewAttached(Screen1::class)
      hasViewVisible(Screen1::class)
    }
    
    with(navigator) {
      hasStackCount(1)
      hasScreensInCacheCount(1)
      hasScreenInStack(0, screen1.screenKey)
      hasScreenInCache(screen1.screenKey)
    }
  }
  
  @Test
  fun `Opening several screens on top of each other`() {
    navigator.applyCommand(Forward(screen1))
    navigator.applyCommand(Forward(screen2))
    navigator.applyCommand(Forward(screen3))
    
    with(host) {
      hasViewCount(3)
      hasViewAttached(Screen1::class)
      hasViewAttached(Screen2::class)
      hasViewAttached(Screen3::class)
      hasViewInvisible(Screen1::class)
      hasViewInvisible(Screen2::class)
      hasViewVisible(Screen3::class)
    }
    
    with(navigator) {
      hasStackCount(3)
      hasScreenInStack(0, screen1.screenKey)
      hasScreenInStack(1, screen2.screenKey)
      hasScreenInStack(2, screen3.screenKey)
      hasScreensInCacheCount(3)
      hasScreenInCache(screen1.screenKey)
      hasScreenInCache(screen2.screenKey)
      hasScreenInCache(screen3.screenKey)
    }
  }
}