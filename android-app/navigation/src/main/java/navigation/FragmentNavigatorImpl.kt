package navigation

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class FragmentNavigatorImpl(
  private val activity: FragmentActivity,
  private val containerViewId: Int,
) : Navigator {
  
  override fun applyCommands(commands: Array<out Command>) {
    commands.forEach { command ->
      when (command) {
        is Forward -> performForward(command.screenInfo.screenKey,
          command.screenInfo.arguments
        )
        is Back -> performBack()
        is BackTo -> performBackTo(command.screenInfo.screenKey)
        is ForwardWithRemovalOf -> performForwardWithRemovalOf(command.screenInfo.screenKey,
          command.screenInfo.arguments, command.screensCount
        )
        is SwitchToNewRoot -> performSwitchToNewRoot(command.screenInfo.screenKey,
          command.screenInfo.arguments
        )
      }
    }
  }
  
  private fun performForward(
    screenKey: ScreenKey,
    arguments: Bundle
  ) {
    fragmentManager.transaction {
      addNewFragment(screenKey, arguments)
    }
  }
  
  private fun performForwardWithRemovalOf(
    screenKey: ScreenKey,
    arguments: Bundle,
    screensToRemove: Int
  ) {
    fragmentManager.transaction {
      repeat(screensToRemove) { index ->
        remove(fragmentManager.fragments[fragmentManager.fragments.lastIndex - index])
      }
      addNewFragment(screenKey, arguments)
    }
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
      fragmentManager.transaction {
        applyBackwardAnimations()
        remove(fragmentManager.fragments.last())
      }
    }
  }
  
  private fun performBackTo(screenKey: ScreenKey) {
    val desiredIndex = fragmentManager.fragments.indexOfFirst { fragment ->
      (fragment as BaseFragmentScreen).screenKey == screenKey
    }
    check(desiredIndex != -1) { "Fragment with key $screenKey was not found" }
    val lastIndex = fragmentManager.fragments.lastIndex
    val topFragment = fragmentManager.fragments.last()
    fragmentManager.transaction {
      repeat(lastIndex - desiredIndex - 1) { index ->
        remove(fragmentManager.fragments[lastIndex - 1 - index])
      }
      runOnCommit {
        topFragment.requireView().animateSlideToRight(200) {
          fragmentManager.transaction { remove(topFragment) }
        }
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
    topFragment.requireView().animateSlideToRight(200) {
      fragmentManager.transaction { remove(topFragment) }
    }
    return hasScreensYet
  }
  
  override fun onSaveInstanceState(bundle: Bundle) {
    // TODO (25.11.2022): Add support in future ?
  }
  
  override fun onRestoreInstanceState(bundle: Bundle?) {
    // TODO (25.11.2022): Add support in future ?
  }
  
  override fun releaseScreens() {
    // TODO (25.11.2022): Add support in future ?
  }
  
  private val fragmentManager get() = activity.supportFragmentManager
  
  private fun FragmentTransaction.addNewFragment(screenKey: ScreenKey, arguments: Bundle) {
    val name = screenKey.screenClassName
    val newFragment = Class.forName(name).newInstance() as BaseFragmentScreen
    newFragment.screenKey = screenKey
    newFragment.arguments = arguments
    applyForwardAnimations()
    add(containerViewId, newFragment, screenKey.toString())
  }
  
  private fun FragmentManager.transaction(block: FragmentTransaction.() -> Unit) {
    beginTransaction()
        .setReorderingAllowed(true)
        .apply(block)
        .commitAllowingStateLoss()
  }
  
  private fun FragmentTransaction.applyForwardAnimations() {
    setCustomAnimations(R.anim.slide_in, R.anim.fade_out)
  }
  
  private fun FragmentTransaction.applyBackwardAnimations() {
    setCustomAnimations(R.anim.fade_in, R.anim.slide_out)
  }
}
