package com.arsvechkarev.vault.core.mvi

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface MviView<S> : MvpView {
  
  @AddToEndSingle
  fun render(state: S)
}