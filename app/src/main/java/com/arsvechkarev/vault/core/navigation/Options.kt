package com.arsvechkarev.vault.core.navigation

class ForwardOptions(
  val arguments: Map<String, Any> = emptyMap(),
  val replaceCurrentScreen: Boolean = false
)

class BackwardOptions(val removeCurrentScreen: Boolean = false)

class BackwardToOptions(val removeAllLeftScreens: Boolean = false)