package com.arsvechkarev.vault.features.passwords_list

import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.model.ServiceInfo
import com.arsvechkarev.vault.features.common.PasswordsListRepository

class PasswordsListPresenter(
  threader: Threader,
  private val passwordsListRepository: PasswordsListRepository
) : BasePresenter<PasswordsListView>(threader) {
  
  private val listChangeListener: (List<ServiceInfo>) -> Unit = { list ->
    viewState.showPasswordsList(list)
  }
  
  override fun onFirstViewAttach() {
    passwordsListRepository.addChangeListener(listChangeListener)
  }
  
  fun loadPasswords(masterPassword: String) {
    onIoThread {
      updateViewState { showLoading() }
      val passwords = passwordsListRepository.getAllServicesInfo(masterPassword)
      if (passwords.isEmpty()) {
        updateViewState { showNoPasswords() }
      } else {
        updateViewState { showPasswordsList(passwords) }
      }
    }
  }
}