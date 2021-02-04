package com.arsvechkarev.vault.features.info

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface InfoView : MvpView {
  
  fun showPasswordEditingDialog(password: String)
  
  fun showLetterChange(letter: String)
  
  fun showServiceName(serviceName: String)
  
  fun showEmail(email: String)
  
  fun showNoEmail()
  
  fun showErrorSavingServiceName(errorText: String)
  
  fun setPassword(password: String)
  
  fun showPassword(password: String)
  
  fun hidePassword()
  
  fun switchFromEditingMode()
  
  fun showLoading()
  
  fun showFinishLoading()
  
  fun closePasswordDialog()
}