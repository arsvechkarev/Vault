package com.arsvechkarev.vault.core.mvi

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

/** Mvi view that only renders state [S] */
interface MviView<State, News> : MvpView {
  
  @AddToEndSingle
  fun render(state: State)
  
  @OneExecution
  fun handleNews(event: News) = Unit
}