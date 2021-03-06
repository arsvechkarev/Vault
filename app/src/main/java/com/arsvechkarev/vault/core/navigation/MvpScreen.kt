package com.arsvechkarev.vault.core.navigation

import androidx.annotation.CallSuper
import moxy.MvpDelegate
import moxy.MvpDelegateHolder

abstract class MvpScreen : Screen, MvpDelegateHolder {
  
  private val onInitPresenterList = ArrayList<(() -> Unit)>()
  private var _mvpDelegate: MvpDelegate<out Screen>? = null
  
  fun whenPresenterIsReady(block: () -> Unit) {
    onInitPresenterList.add(block)
  }
  
  override fun getMvpDelegate(): MvpDelegate<*> {
    return _mvpDelegate ?: run {
      _mvpDelegate = MvpDelegate(this)
      _mvpDelegate!!
    }
  }
  
  @CallSuper
  override fun onInit(arguments: Map<String, Any>) {
    mvpDelegate.onCreate()
    onInitPresenterList.forEach { function -> function() }
    onInitPresenterList.clear()
  }
  
  @CallSuper
  override fun onAppearedOnScreen(arguments: Map<String, Any>) {
    mvpDelegate.onAttach()
  }
  
  @CallSuper
  override fun onDisappearedFromScreen() {
    mvpDelegate.onSaveInstanceState()
    mvpDelegate.onDetach()
  }
  
  @CallSuper
  override fun onRelease() {
    mvpDelegate.onDestroyView()
    mvpDelegate.onDestroy()
  }
  
  private fun onInitDelegate() {
    mvpDelegate.onCreate()
    onInitPresenterList.forEach { function -> function() }
    onInitPresenterList.clear()
  }
  
  private fun onAppearedOnScreenDelegate() {
    mvpDelegate.onAttach()
  }
  
  private fun onDetachDelegate() {
    mvpDelegate.onSaveInstanceState()
    mvpDelegate.onDetach()
  }
  
  private fun onDestroyDelegate() {
    mvpDelegate.onDestroyView()
    mvpDelegate.onDestroy()
  }
}