package com.arsvechkarev.vault.features.info

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface InfoView : MvpView {
  
  fun showServiceName(serviceName: String)
  
  fun showServiceIcon(serviceName: String)
  
  fun showUsername(username: String)
  
  fun showNoUsername()
  
  fun showEmail(email: String)
  
  fun showNoEmail()
  
  fun setPassword(password: String)
  
  fun restoreInitialData()
  
  fun showPassword(password: String)
  
  fun hidePassword()
  
  fun showDeleteDialog(serviceName: String)
  
  fun hideDeleteDialog()
  
  fun showLoading()
  
  fun hideLoading()
  
  fun showCopiedPassword()
  
  fun showExit()
}