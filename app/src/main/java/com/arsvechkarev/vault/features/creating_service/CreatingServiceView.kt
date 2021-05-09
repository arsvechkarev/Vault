package com.arsvechkarev.vault.features.creating_service

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface CreatingServiceView : MvpView {
  
  fun showServiceNameCannotBeEmpty()
  
  fun showServiceIcon(serviceName: String)
}