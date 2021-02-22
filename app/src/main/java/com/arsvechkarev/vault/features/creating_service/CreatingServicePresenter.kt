package com.arsvechkarev.vault.features.creating_service

import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.model.Service
import com.arsvechkarev.vault.cryptography.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.features.common.ServicesRepository
import com.arsvechkarev.vault.features.common.getIconForServiceName
import com.arsvechkarev.vault.features.creating_service.CreatingServiceScreenState.DIALOG_PASSWORD_STRENGTH
import com.arsvechkarev.vault.features.creating_service.CreatingServiceScreenState.DIALOG_SAVE_PASSWORD
import com.arsvechkarev.vault.features.creating_service.CreatingServiceScreenState.INITIAL
import com.arsvechkarev.vault.features.creating_service.CreatingServiceScreenState.PASSWORD_SCREEN
import java.util.UUID

class CreatingServicePresenter(
  private val servicesRepository: ServicesRepository,
  threader: Threader
) : BasePresenter<CreatingServiceView>(threader) {
  
  private var state = INITIAL
  private var serviceName: String = ""
  private var username: String = ""
  private var email: String = ""
  private var password: String = ""
  
  fun onServiceNameChanged(text: String) {
    if (text.isNotBlank()) {
      val icon = getIconForServiceName(text)
      if (icon != null) {
        viewState.showIconFromResources(icon)
      } else {
        viewState.showLetterInCircleIcon(text[0].toString())
      }
    } else {
      viewState.hideLetterInCircleIcon()
    }
  }
  
  fun onContinueClicked(serviceName: String, username: String, email: String) {
    if (serviceName.isBlank()) {
      viewState.showServiceNameCannotBeEmpty()
      return
    }
    state = PASSWORD_SCREEN
    this.serviceName = serviceName.trim()
    this.username = username.trim()
    this.email = email.trim()
    viewState.showPasswordCreatingDialog()
  }
  
  fun onPasswordEntered(password: String) {
    this.password = password
    state = DIALOG_SAVE_PASSWORD
    viewState.showSavePasswordDialog()
  }
  
  fun onPasswordIsTooWeakClicked() {
    state = DIALOG_PASSWORD_STRENGTH
    viewState.showPasswordStrengthDialog()
  }
  
  fun onHidePasswordStrengthDialog() {
    state = PASSWORD_SCREEN
    viewState.hidePasswordStrengthDialog()
  }
  
  fun acceptNewPassword() {
    state = INITIAL
    viewState.showLoadingCreation()
    viewState.hideSavePasswordDialog()
    onIoThread {
      val serviceInfo = Service(UUID.randomUUID().toString(), serviceName,
        username, email, password)
      servicesRepository.saveService(masterPassword, serviceInfo)
      updateViewState { showExit() }
    }
  }
  
  fun closePasswordEditingDialog() {
    state = INITIAL
    viewState.hidePasswordCreatingDialog()
  }
  
  fun allowBackPress(): Boolean {
    return when (state) {
      INITIAL -> {
        true
      }
      PASSWORD_SCREEN -> {
        state = INITIAL
        viewState.hidePasswordCreatingDialog()
        false
      }
      DIALOG_PASSWORD_STRENGTH -> {
        state = PASSWORD_SCREEN
        viewState.hidePasswordStrengthDialog()
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