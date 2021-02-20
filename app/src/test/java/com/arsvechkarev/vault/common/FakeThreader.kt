package com.arsvechkarev.vault.common

import com.arsvechkarev.vault.core.Threader

object FakeThreader : Threader {
  
  override fun onBackgroundThread(block: () -> Unit) = block()
  
  override fun onIoThread(block: () -> Unit) = block()
  
  override fun onMainThread(block: () -> Unit) = block()
}