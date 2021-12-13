package navigation

import com.github.terrakok.cicerone.Command
import navigation.fakes.FakeNavigationHost
import navigation.fakes.FakeScreenHandler
import navigation.fakes.FakeViewHavingScreen
import navigation.fakes.LifecycleState
import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import kotlin.reflect.KClass

fun KClass<out Screen>.getViewTag(): String = java.name

fun ExtendedNavigatorImpl.applyCommand(command: Command) {
  applyCommands(arrayOf(command))
}

fun ExtendedNavigatorImpl.hasStackCount(count: Int) {
  assertTrue(_test_screensStack.size == count)
}

fun ExtendedNavigatorImpl.hasScreenInStack(position: Int, screenKey: ScreenKey) {
  assertTrue(_test_screensStack[position] == screenKey)
}

fun ExtendedNavigatorImpl.hasScreensInCacheCount(count: Int) {
  assertTrue(_test_screenHandlersCache.size == count)
}

fun ExtendedNavigatorImpl.hasScreenInCache(screenKey: ScreenKey) {
  assertTrue(_test_screenHandlersCache.containsKey(screenKey))
}

fun ExtendedNavigatorImpl.hasScreenState(screenKey: ScreenKey, state: LifecycleState) {
  val handler = _test_screenHandlersCache.getValue(screenKey)
  val screen = (handler as FakeScreenHandler).screen
  Assert.assertEquals((screen as FakeViewHavingScreen).state, state)
}

fun FakeNavigationHost.hasViewCount(count: Int) {
  assertTrue(views.size == count)
}

fun FakeNavigationHost.hasViewAttached(screenClass: KClass<out Screen>) {
  assertTrue(views.find { it.tag == screenClass.getViewTag() }!!.isAttachedToWindow)
}

fun FakeNavigationHost.hasViewNotAttached(screenClass: KClass<out Screen>) {
  assertFalse(views.find { it.tag == screenClass.getViewTag() }!!.isAttachedToWindow)
}

fun FakeNavigationHost.hasViewVisible(screenClass: KClass<out Screen>) {
  assertTrue(views.find { it.tag == screenClass.getViewTag() }!!.isVisible)
}

fun FakeNavigationHost.hasViewInvisible(screenClass: KClass<out Screen>) {
  assertFalse(views.find { it.tag == screenClass.getViewTag() }!!.isVisible)
}