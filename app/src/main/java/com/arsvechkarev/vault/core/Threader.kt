package com.arsvechkarev.vault.core

/**
 * Executes a given lambda on a specified thread
 *
 * @see AndroidThreader
 */
interface Threader {
  
  /**
   * Executes [block] on a background thread
   */
  fun onBackgroundThread(block: () -> Unit)
  
  /**
   * Executes [block] on a dedicated IO thread
   */
  fun onIoThread(block: () -> Unit)
  
  /**
   * Executes [block] on the Android main thread
   */
  fun onMainThread(block: () -> Unit)
}