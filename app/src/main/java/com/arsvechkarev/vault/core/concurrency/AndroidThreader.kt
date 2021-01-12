package com.arsvechkarev.vault.core.concurrency

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors
import java.util.concurrent.Future

object AndroidThreader : Threader {
  
  private val backgroundWorker = Executors.newSingleThreadExecutor()
  private val ioWorker = Executors.newFixedThreadPool(4)
  
  private val handler = Handler(Looper.getMainLooper())
  
  override fun onBackground(block: () -> Unit): Future<*> {
    return backgroundWorker.submit(block)
  }
  
  override fun onIoThread(block: () -> Unit): Future<*> {
    return ioWorker.submit(block)
  }
  
  override fun onMainThread(block: () -> Unit) {
    handler.post(block)
  }
}