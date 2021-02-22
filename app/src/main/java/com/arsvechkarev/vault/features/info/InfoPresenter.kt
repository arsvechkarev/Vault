package com.arsvechkarev.vault.features.info

import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Clipboard
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.model.Service
import com.arsvechkarev.vault.cryptography.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.features.common.ServicesRepository
import com.arsvechkarev.vault.features.common.getIconForServiceName
import com.arsvechkarev.vault.features.info.InfoScreenState.DELETING_DIALOG
import com.arsvechkarev.vault.features.info.InfoScreenState.EDITING_NAME_OR_USERNAME_OR_EMAIL
import com.arsvechkarev.vault.features.info.InfoScreenState.INITIAL
import com.arsvechkarev.vault.features.info.InfoScreenState.LOADING
import com.arsvechkarev.vault.features.info.InfoScreenState.PASSWORD_EDITING_DIALOG
import com.arsvechkarev.vault.features.info.InfoScreenState.PASSWORD_STRENGTH_DIALOG
import com.arsvechkarev.vault.features.info.InfoScreenState.SAVE_PASSWORD_DIALOG

class InfoPresenter(
  private val servicesRepository: ServicesRepository,
  private val clipboard: Clipboard,
  threader: Threader
) : BasePresenter<InfoView>(threader) {
  
  private lateinit var service: Service
  
  private var password: String = ""
  private var state = INITIAL
  
  val isEditingNameOrEmailNow get() = state == EDITING_NAME_OR_USERNAME_OR_EMAIL
  
  fun performSetup(service: Service) {
    this.service = service
    state = INITIAL
    password = service.password
    updateServiceIcon(service.serviceName)
    viewState.showServiceName(service.serviceName)
    viewState.setPassword(service.password)
    val username = service.username
    if (username.isEmpty()) viewState.showNoUsername() else viewState.showUsername(username)
    val email = service.email
    if (email.isEmpty()) viewState.showNoEmail() else viewState.showEmail(email)
  }
  
  fun onServiceNameChanged(text: String) {
    if (text.isBlank()) return
    updateServiceIcon(text)
  }
  
  fun saveServiceName(serviceName: String) {
    state = INITIAL
    if (service.serviceName == serviceName) return
    viewState.showLoading()
    state = LOADING
    service = Service(service.id, serviceName, service.username,
      service.email, service.password)
    viewState.showServiceName(serviceName)
    updateServiceIcon(serviceName)
    onIoThread {
      servicesRepository.updateService(masterPassword, service)
      state = INITIAL
      updateViewState { hideLoading() }
    }
  }
  
  fun saveUsername(username: String) {
    state = INITIAL
    if (service.username == username) return
    viewState.showLoading()
    state = LOADING
    service = Service(service.id, service.serviceName, username,
      service.email, service.password)
    if (username.isBlank()) viewState.showNoUsername() else viewState.showUsername(username)
    onIoThread {
      servicesRepository.updateService(masterPassword, service)
      state = INITIAL
      updateViewState { hideLoading() }
    }
  }
  
  fun saveEmail(email: String) {
    state = INITIAL
    if (service.email == email) return
    viewState.showLoading()
    state = LOADING
    service = Service(service.id, service.serviceName, service.username,
      email, service.password)
    if (email.isBlank()) viewState.showNoEmail() else viewState.showEmail(email)
    onIoThread {
      servicesRepository.updateService(masterPassword, service)
      state = INITIAL
      updateViewState { hideLoading() }
    }
  }
  
  fun onTogglePassword(isPasswordShown: Boolean) {
    if (isPasswordShown) {
      viewState.showPassword(service.password)
    } else {
      viewState.hidePassword()
    }
  }
  
  fun switchToEditingMode() {
    state = EDITING_NAME_OR_USERNAME_OR_EMAIL
  }
  
  fun onDeleteClicked() {
    state = DELETING_DIALOG
    viewState.showDeleteDialog(service.serviceName)
  }
  
  fun onHideDeleteDialog() {
    state = INITIAL
    viewState.hideDeleteDialog()
  }
  
  fun agreeToDeleteService() {
    viewState.hideDeleteDialog()
    viewState.showLoading()
    onIoThread {
      servicesRepository.deleteService(masterPassword, service, notifyListeners = true)
      state = INITIAL
      updateViewState { showExit() }
    }
  }
  
  fun onCopyClicked() {
    clipboard.copyPassword(service.password)
    viewState.showCopiedPassword()
  }
  
  fun onEditPasswordIconClicked() {
    state = PASSWORD_EDITING_DIALOG
    viewState.showPasswordEditingDialog(service.password)
  }
  
  fun onShowPasswordStrengthDialog() {
    state = PASSWORD_STRENGTH_DIALOG
    viewState.showPasswordStrengthDialog()
  }
  
  fun onHidePasswordStrengthDialog() {
    state = PASSWORD_EDITING_DIALOG
    viewState.hidePasswordStrengthDialog()
  }
  
  fun onSaveNewPasswordClicked(password: String) {
    if (this.password == password) {
      state = INITIAL
      viewState.hidePasswordEditingDialog()
      return
    }
    if (this.password.isEmpty()) {
      // Password was cleared after user closed save password dialog
      this.password = password
    }
    this.password = password
    state = SAVE_PASSWORD_DIALOG
    viewState.showAcceptPasswordDialog()
  }
  
  fun acceptPassword() {
    state = LOADING
    viewState.showLoading()
    viewState.hideSavePasswordDialog()
    service = Service(service.id, service.serviceName, service.username,
      service.email, password)
    onIoThread {
      servicesRepository.updateService(masterPassword, service)
      state = INITIAL
      updateViewState {
        setPassword(password)
        hidePasswordEditingDialog()
        hideLoading()
      }
    }
  }
  
  fun closePasswordScreen() {
    state = INITIAL
    viewState.hidePasswordEditingDialog()
  }
  
  fun onHideAcceptPasswordDialog() {
    password = ""
    state = PASSWORD_EDITING_DIALOG
    viewState.hideSavePasswordDialog()
  }
  
  fun allowBackPress(): Boolean {
    return when (state) {
      INITIAL -> true
      LOADING -> false
      EDITING_NAME_OR_USERNAME_OR_EMAIL -> {
        viewState.restoreInitialData()
        state = INITIAL
        false
      }
      DELETING_DIALOG -> {
        viewState.hideDeleteDialog()
        state = INITIAL
        false
      }
      PASSWORD_EDITING_DIALOG -> {
        viewState.hidePasswordEditingDialog()
        state = INITIAL
        false
      }
      PASSWORD_STRENGTH_DIALOG -> {
        viewState.hidePasswordStrengthDialog()
        state = PASSWORD_EDITING_DIALOG
        false
      }
      SAVE_PASSWORD_DIALOG -> {
        viewState.hideSavePasswordDialog()
        state = PASSWORD_EDITING_DIALOG
        false
      }
    }
  }
  
  private fun updateServiceIcon(serviceName: String) {
    val icon = getIconForServiceName(serviceName)
    if (icon != null) {
      viewState.showIconFromResources(icon)
    } else {
      viewState.showLetterInCircleIcon(serviceName[0].toString())
    }
  }
}