package com.arsvechkarev.vault.features.info

import android.graphics.drawable.Drawable
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface InfoView : MvpView {
  
  fun showLetterInCircleIcon(letter: String)
  
  fun showIconFromResources(icon: Drawable)
  
  fun showServiceName(serviceName: String)
  
  fun showEmail(email: String)
  
  fun showNoEmail()
  
  fun setPassword(password: String)
  
  fun showPassword(password: String)
  
  fun hidePassword()
  
  fun showDeleteDialog(serviceName: String)
  
  fun hideDeleteDialog()
  
  fun showPasswordEditingDialog(password: String)
  
  fun hidePasswordEditingDialog()
  
  fun showAcceptPasswordDialog()
  
  fun hideSavePasswordDialog()
  
  fun showLoading()
  
  fun showFinishLoading()
  
  fun showExit()
}