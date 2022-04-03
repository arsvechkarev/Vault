package com.arsvechkarev.vault.core.mvi

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

/** Mvi view that only renders state [S] */
interface MviView<S> : MvpView {

    @AddToEndSingle
    fun render(state: S)

    @OneExecution
    fun renderSingleEvent(event: Any) = Unit
}