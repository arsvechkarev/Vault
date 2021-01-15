package com.arsvechkarev.vault.core

import android.os.Handler
import android.os.Looper
import androidx.annotation.CallSuper
import moxy.MvpPresenter
import moxy.MvpView
import java.util.concurrent.Executors

abstract class BasePresenter<V : MvpView>(
  protected val threader: Threader
) : MvpPresenter<V>() {
  
  private val backgroundWorker = Executors.newSingleThreadExecutor()
  private val ioWorker = Executors.newFixedThreadPool(4)
  private val handler = Handler(Looper.getMainLooper())
  
  protected fun onBackground(block: () -> Unit) {
    backgroundWorker.submit(block)
  }
  
  protected fun onIoThread(block: () -> Unit) {
    ioWorker.submit(block)
  }
  
  protected fun onMainThread(block: () -> Unit) {
    handler.post(block)
  }
  
  protected fun updateViewState(block: V.() -> Unit) {
    viewState.apply(block)
  }
  
  @CallSuper
  override fun onDestroy() {
    backgroundWorker.shutdownNow()
    ioWorker.shutdownNow()
    handler.removeCallbacksAndMessages(null)
  }
}