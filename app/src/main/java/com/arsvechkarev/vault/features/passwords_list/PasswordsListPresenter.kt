package com.arsvechkarev.vault.features.passwords_list

import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.model.Service
import com.arsvechkarev.vault.cryptography.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.features.common.ServicesRepository

class PasswordsListPresenter(
  threader: Threader,
  private val servicesRepository: ServicesRepository
) : BasePresenter<PasswordsListView>(threader) {
  
  private val listChangeListener: (List<Service>) -> Unit = { list ->
    viewState.showPasswordsList(list)
  }
  
  override fun onFirstViewAttach() {
    servicesRepository.addChangeListener(listChangeListener)
  }
  
  fun startLoadingPasswords() {
    onIoThread {
      updateViewState { showLoading() }
      val passwords = servicesRepository.getServices(masterPassword)
      if (passwords.isEmpty()) {
        updateViewState { showNoPasswords() }
      } else {
        updateViewState { showPasswordsList(passwords) }
      }
    }
  }
  
  fun onLongClick(service: Service) {
    viewState.showDeleteDialog(service)
  }
  
  fun deleteService(service: Service) {
    viewState.showLoadingDeletingService()
    onIoThread {
      servicesRepository.deleteService(masterPassword, service,
        notifyListeners = false)
      updateViewState { showDeletedService(service) }
    }
  }
}