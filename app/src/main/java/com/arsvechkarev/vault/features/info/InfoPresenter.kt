package com.arsvechkarev.vault.features.info

import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Threader

class InfoPresenter(threader: Threader) : BasePresenter<InfoView>(threader) {
  
  var isEditingSomethingNow: Boolean = false
    private set
  
  fun onTogglePassword(isPasswordShown: Boolean) {
    if (isPasswordShown) {
      viewState.showPassword("Somethinf nice")
    } else {
      viewState.hidePassword()
    }
  }
  
  fun switchToEditingMode() {
    isEditingSomethingNow = true
  }
  
  fun switchFromEditingMode() {
    isEditingSomethingNow = false
  }
}