package com.arsvechkarev.vault.core.navigation

import kotlin.reflect.KClass

/**
 * Represents an app navigator
 */
interface Navigator {
  
  /**
   * Navigates to [screenClass] with [options]:
   *
   * ---------------- Example 1 ----------------
   *
   *                    current
   *                       |
   * Before stack: [1] -> [2]
   *
   * goBackTo(3, operation = Operation.ADD)
   *
   *                          current
   *                             |
   * After stack: [1] -> [2] -> [3]
   *
   *
   *
   * ---------------- Example 2 ----------------
   *
   *                    current
   *                       |
   * Before stack: [1] -> [2]
   *
   * goBackTo(3, operation = Operation.REPLACE)
   *
   *                   current
   *                      |
   * After stack: [1] -> [3]
   */
  fun goTo(screenClass: KClass<out Screen>, options: ForwardOptions = ForwardOptions())
  
  /**
   * Pops current screen with [options]
   */
  fun goBack(options: BackwardOptions = BackwardOptions())
  
  /**
   * Navigates back to a screen in stack with [screenClass] and [options]
   *
   * ---------------- Example 1 ----------------
   *                                  current
   *                                     |
   * Before stack: [1] -> [2] -> [3] -> [4]
   *
   * goBackTo(2, removeCurrentScreen = false)
   *
   *                   current
   *                      |
   * After stack: [1] -> [2]
   *
   *
   *
   * ---------------- Example 2 ----------------
   *
   *                                  current
   *                                     |
   * Before stack: [1] -> [2] -> [3] -> [4]
   *
   * goBackTo(2, removeCurrentScreen = true)
   *
   *                   current
   *                      |
   * After stack: [1] -> [2] -> [3] -> [4]
   */
  fun goBackTo(screenClass: KClass<out Screen>, options: BackwardToOptions = BackwardToOptions())
  
  /**
   * Handles going back in stack. Returns true, if there was any screens in stack to be removed
   * (and removes them), or if current screen handled back interaction. Returns false otherwise
   */
  fun handleGoBack(): Boolean
  
  /**
   * Switches to new navigation root
   *
   * Example:
   *
   * Before stack: [1] -> [2] -> [3]
   *
   * switchToNewRoot(4)
   *
   * After stack: [4]
   */
  fun switchToNewRoot(screenClass: KClass<out Screen>)
}