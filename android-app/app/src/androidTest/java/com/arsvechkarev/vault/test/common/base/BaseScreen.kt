package com.arsvechkarev.vault.test.common.base

import com.kaspersky.kaspresso.screens.KScreen

abstract class BaseScreen<S : BaseScreen<S>> : KScreen<S>() {
  
  override val layoutId = null
}
