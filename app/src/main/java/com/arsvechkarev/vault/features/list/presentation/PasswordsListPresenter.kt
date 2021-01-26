package com.arsvechkarev.vault.features.list.presentation

import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.JSON_SERVICE_NAME
import com.arsvechkarev.vault.core.JSON_SERVICE_PASSWORD
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.extensions.transformToList
import com.arsvechkarev.vault.core.model.ServiceInfo
import com.arsvechkarev.vault.features.list.domain.PasswordsListRepository

class PasswordsListPresenter(
  threader: Threader,
  private val passwordsListRepository: PasswordsListRepository
) : BasePresenter<PasswordsListView>(threader) {
  
  private var servicesInfoList: List<ServiceInfo> = ArrayList()
  
  fun loadPasswords(masterPassword: String) {
    onIoThread {
      updateViewState { showLoading() }
      val passwords = passwordsListRepository.getAllPasswords(masterPassword)
      if (passwords.length() == 0) {
        updateViewState { showNoPasswords() }
      } else {
        servicesInfoList = passwords.transformToList { jsonObject ->
          ServiceInfo(
            jsonObject.getString(JSON_SERVICE_NAME),
            jsonObject.getString(JSON_SERVICE_PASSWORD),
          )
        }
        updateViewState { showPasswordsList(servicesInfoList!!) }
      }
    }
  }
  
  fun onNewServiceButtonClick() {
    viewState.showEnterServiceNameDialog(servicesInfoList)
  }
}