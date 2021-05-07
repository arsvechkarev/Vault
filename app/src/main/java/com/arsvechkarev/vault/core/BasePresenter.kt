package com.arsvechkarev.vault.core

import moxy.MvpPresenter
import moxy.MvpView

abstract class BasePresenter<V : MvpView>(
  protected val threader: Threader
) : MvpPresenter<V>() {
  
  protected fun onBackgroundThread(block: () -> Unit) {
    threader.onBackgroundThread(block)
  }
  
  protected fun onIoThread(block: () -> Unit) {
    threader.onIoThread(block)
  }
  
  protected fun onMainThread(block: () -> Unit) {
    threader.onMainThread(block)
  }
}