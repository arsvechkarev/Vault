package com.arsvechkarev.vault.features.creating_service

import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.model.ServiceInfo
import com.arsvechkarev.vault.cryptography.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.features.common.PasswordsListRepository
import com.arsvechkarev.vault.features.creating_service.CreatingServiceScreenState.DIALOG_SAVE_PASSWORD
import com.arsvechkarev.vault.features.creating_service.CreatingServiceScreenState.INITIAL
import com.arsvechkarev.vault.features.creating_service.CreatingServiceScreenState.PASSWORD_SCREEN
import java.util.UUID

class CreatingServicePresenter(
  private val passwordsListRepository: PasswordsListRepository,
  threader: Threader
) : BasePresenter<CreatingServiceView>(threader) {
  
  private var state = INITIAL
  private var serviceName: String = ""
  private var email: String = ""
  private var password: String = ""
  
  fun onContinueClicked(serviceName: String, email: String) {
    if (serviceName.isBlank()) {
      viewState.showServiceNameCannotBeEmpty()
      return
    }
    state = PASSWORD_SCREEN
    val passwords = passwordsListRepository.getAllServicesInfo(masterPassword)
    for (serviceInfo in passwords) {
      if (serviceInfo.name == serviceName) {
        viewState.showServiceNameAlreadyExists()
        return
      }
    }
    this.serviceName = serviceName.trim()
    this.email = email.trim()
    viewState.showPasswordCreatingDialog()
  }
  
  fun onPasswordEntered(password: String) {
    this.password = password
    state = DIALOG_SAVE_PASSWORD
    viewState.showDialogSavePassword()
  }
  
  fun acceptNewPassword() {
    state = INITIAL
    viewState.showLoadingCreation()
    viewState.hideSavePasswordDialog()
    onIoThread {
      val serviceInfo = ServiceInfo(UUID.randomUUID().toString(), serviceName, email, password)
      passwordsListRepository.saveServiceInfo(masterPassword, serviceInfo)
      updateViewState { showExit() }
    }
  }
  
  fun closePasswordEditingDialog() {
    state = INITIAL
    viewState.hidePasswordEditingDialog()
  }
  
  fun allowBackPress(): Boolean {
    return when (state) {
      INITIAL -> {
        true
      }
      PASSWORD_SCREEN -> {
        state = INITIAL
        viewState.hidePasswordEditingDialog()
        false
      }
      DIALOG_SAVE_PASSWORD -> {
        state = PASSWORD_SCREEN
        viewState.hideSavePasswordDialog()
        false
      }
    }
  }
}