package com.arsvechkarev.vault.features.passwords_list

import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.features.common.PasswordsListCachingRepository

class PasswordsListPresenter(
  threader: Threader,
  private val passwordsListCachingRepository: PasswordsListCachingRepository
) : BasePresenter<PasswordsListView>(threader) {
  
  fun loadPasswords(masterPassword: String) {
    onIoThread {
      updateViewState { showLoading() }
      val passwords = passwordsListCachingRepository.getAllServicesInfo(masterPassword)
      if (passwords.isEmpty()) {
        updateViewState { showNoPasswords() }
      } else {
        updateViewState { showPasswordsList(passwords) }
      }
    }
  }
  
  fun onNewServiceButtonClick() {
    viewState.showEnterServiceNameDialog()
  }
}