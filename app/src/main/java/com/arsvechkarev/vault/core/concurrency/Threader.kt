package com.arsvechkarev.vault.core.concurrency

import java.util.concurrent.Future

/**
 * Executes a given lambda on a specified thread
 *
 * @see AndroidThreader
 */
interface Threader {
  
  /**
   * Executes [block] on a background thread and returns a future that represents that task
   */
  fun onBackground(block: () -> Unit): Future<*>
  
  /**
   * Executes [block] on a dedicated IO thread and returns a future that represents that task
   */
  fun onIoThread(block: () -> Unit): Future<*>
  
  /**
   * Executes [block] on the Android main thread
   */
  fun onMainThread(block: () -> Unit)
}