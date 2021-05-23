package com.arsvechkarev.vault.features.password_checking

interface PasswordCheckingView {
  
  fun showDialog()
  
  fun hideDialog()
  
  fun showPasswordCheckingLoading()
  
  fun showPasswordCheckingFinished()
  
  fun showPasswordIsIncorrect()
}