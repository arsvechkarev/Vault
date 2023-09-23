package navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class FragmentNavigatorImpl(
  private val activity: FragmentActivity,
  private val containerViewId: Int,
) : Navigator {
  
  private val fragmentManager get() = activity.supportFragmentManager
  
  override fun applyCommands(commands: Array<out Command>) {
    commands.forEach { command ->
      when (command) {
        is Forward -> performForward(command.screenInfo.screenKey, command.screenInfo.arguments,
          command.animate)
        is Back -> performBack()
        is BackTo -> performBackTo(command.screenInfo.screenKey)
        is ForwardWithRemovalOf -> {
          performForwardWithRemovalOf(
            command.screenInfo.screenKey,
            command.screenInfo.arguments,
            command.screensCount
          )
        }
        
        is SwitchToNewRoot -> performSwitchToNewRoot(command.screenInfo.screenKey,
          command.screenInfo.arguments
        )
      }
    }
  }
  
  private fun performForward(
    screenKey: ScreenKey,
    arguments: Bundle,
    animate: Boolean
  ) {
    fragmentManager.transaction {
      addNewFragment(screenKey, arguments, animate)
    }
  }
  
  private fun performForwardWithRemovalOf(
    screenKey: ScreenKey,
    arguments: Bundle,
    screensToRemove: Int
  ) {
    fragmentManager.transaction {
      addNewFragment(screenKey, arguments)
    }
    activity.window.decorView.postDelayed({
      fragmentManager.transaction {
        repeat(screensToRemove) { index ->
          remove(fragmentManager.fragments[fragmentManager.fragments.lastIndex - 1 - index])
        }
      }
    }, (animationDuration * 1.2).toLong())
  }
  
  private fun performSwitchToNewRoot(screenKey: ScreenKey, arguments: Bundle) {
    fragmentManager.transaction {
      fragmentManager.fragments.forEach(::remove)
      addNewFragment(screenKey, arguments)
    }
  }
  
  private fun performBack() {
    require(fragmentManager.fragments.isNotEmpty())
    if (fragmentManager.fragments.size == 1) {
      activity.finish()
    } else {
      fragmentManager.removeTopFragmentWithAnimation()
    }
  }
  
  private fun performBackTo(screenKey: ScreenKey) {
    val desiredIndex = fragmentManager.fragments.indexOfFirst { fragment ->
      (fragment as BaseFragmentScreen).screenKey == screenKey
    }
    check(desiredIndex != -1) { "Fragment with key $screenKey was not found" }
    val lastIndex = fragmentManager.fragments.lastIndex
    fragmentManager.transaction {
      repeat(lastIndex - desiredIndex - 1) { index ->
        remove(fragmentManager.fragments[lastIndex - 1 - index])
      }
      runOnCommit {
        fragmentManager.removeTopFragmentWithAnimation()
      }
    }
  }
  
  override fun handleGoBack(): Boolean {
    val fragments = fragmentManager.fragments
    if (fragments.isEmpty()) {
      return false
    }
    val topFragment = fragments.last()
    val handled = (topFragment as BaseFragmentScreen).handleBackPress()
    if (handled) {
      return true
    }
    val hasScreensYet = fragmentManager.fragments.size > 1
    if (hasScreensYet) {
      fragmentManager.removeTopFragmentWithAnimation()
    }
    return hasScreensYet
  }
  
  private fun FragmentTransaction.addNewFragment(
    screenKey: ScreenKey,
    arguments: Bundle,
    animate: Boolean = true
  ) {
    val name = screenKey.screenClassName
    val newFragment = Class.forName(name).newInstance() as BaseFragmentScreen
    newFragment.screenKey = screenKey
    newFragment.arguments = arguments
    if (animate) {
      setCustomAnimations(R.anim.slide_in, R.anim.fade_out)
    }
    add(containerViewId, newFragment, screenKey.toString())
  }
  
  private fun FragmentManager.removeTopFragmentWithAnimation(
    fragment: Fragment = fragments.last()
  ) {
    fragment.requireView().animateSlideToRight(animationDuration.toLong()) {
      fragmentManager.transaction { remove(fragment) }
    }
  }
  
  private fun FragmentManager.transaction(block: FragmentTransaction.() -> Unit) {
    beginTransaction()
        .setReorderingAllowed(true)
        .apply(block)
        .commitAllowingStateLoss()
  }
  
  private val animationDuration
    get() = activity.resources.getInteger(R.integer.navigation_animation_duration)
}
