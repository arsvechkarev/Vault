package com.arsvechkarev.vault.features.settings.presentation

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SettingsView : MvpView {
  
  fun showUseFingerprintForEnteringEnabled(checked: Boolean)
}