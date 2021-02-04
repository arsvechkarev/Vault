package com.arsvechkarev.vault.features.creating_service

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface CreatingServiceView : MvpView {
  
  fun showServiceNameCannotBeEmpty()
  
  fun showServiceNameAlreadyExists()
  
  fun showPasswordCreatingDialog()
  
  fun showLoadingCreation()
  
  fun showDialogSavePassword()
  
  fun hidePasswordEditingDialog()
  
  fun hideSavePasswordDialog()
  
  fun showExite()
}