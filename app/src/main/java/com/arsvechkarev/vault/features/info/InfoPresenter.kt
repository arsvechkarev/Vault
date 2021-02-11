package com.arsvechkarev.vault.features.info

import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.model.ServiceInfo
import com.arsvechkarev.vault.cryptography.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.features.common.PasswordsListRepository
import com.arsvechkarev.vault.features.info.InfoScreenState.EDITING_NAME_OR_EMAIL
import com.arsvechkarev.vault.features.info.InfoScreenState.ERROR_EDITING_NAME
import com.arsvechkarev.vault.features.info.InfoScreenState.INITIAL
import com.arsvechkarev.vault.features.info.InfoScreenState.LOADING
import com.arsvechkarev.vault.features.info.InfoScreenState.PASSWORD_EDITING_DIALOG
import com.arsvechkarev.vault.features.info.InfoScreenState.SAVE_PASSWORD_DIALOG

class InfoPresenter(
  private val passwordsListRepository: PasswordsListRepository,
  threader: Threader
) : BasePresenter<InfoView>(threader) {
  
  private lateinit var serviceInfo: ServiceInfo
  private var password: String = ""
  private var state = INITIAL
  
  val isEditingNameOrEmailNow get() = state == EDITING_NAME_OR_EMAIL
  
  fun performSetup(serviceInfo: ServiceInfo) {
    this.serviceInfo = serviceInfo
    password = serviceInfo.password
    viewState.showLetterChange(serviceInfo.name[0].toString())
    viewState.showServiceName(serviceInfo.name)
    viewState.setPassword(serviceInfo.password)
    val email = serviceInfo.email
    if (email.isEmpty()) viewState.showNoEmail() else viewState.showEmail(email)
  }
  
  fun onServiceNameSavingAllowed(serviceName: String): Boolean {
    if (serviceName == serviceInfo.name) return true
    val servicesInfo = passwordsListRepository.getAllServicesInfo(masterPassword)
    if (servicesInfo.find { it.name == serviceName } != null) {
      state = ERROR_EDITING_NAME
      viewState.showErrorSavingServiceName(serviceName)
      return false
    }
    return true
  }
  
  fun saveServiceName(serviceName: String) {
    state = INITIAL
    if (serviceInfo.name == serviceName) return
    viewState.showLoading()
    state = LOADING
    serviceInfo = ServiceInfo(serviceInfo.id, serviceName, serviceInfo.email, serviceInfo.password)
    viewState.showServiceName(serviceName)
    viewState.showLetterChange(serviceName[0].toString())
    onIoThread {
      passwordsListRepository.updateServiceInfo(masterPassword, serviceInfo)
      state = INITIAL
      updateViewState { showFinishLoading() }
    }
  }
  
  fun saveEmail(email: String) {
    state = INITIAL
    if (serviceInfo.email == email) return
    viewState.showLoading()
    state = LOADING
    serviceInfo = ServiceInfo(serviceInfo.id, serviceInfo.name, email, serviceInfo.password)
    if (email.isBlank()) viewState.showNoEmail() else viewState.showEmail(email)
    onIoThread {
      passwordsListRepository.updateServiceInfo(masterPassword, serviceInfo)
      state = INITIAL
      updateViewState { showFinishLoading() }
    }
  }
  
  fun onTogglePassword(isPasswordShown: Boolean) {
    if (isPasswordShown) {
      viewState.showPassword(serviceInfo.password)
    } else {
      viewState.hidePassword()
    }
  }
  
  fun switchToEditingMode() {
    state = EDITING_NAME_OR_EMAIL
  }
  
  fun onEditPasswordIconClicked() {
    state = PASSWORD_EDITING_DIALOG
    viewState.showPasswordEditingDialog(serviceInfo.password)
  }
  
  fun onSaveNewPasswordClicked(password: String) {
    if (this.password == password) {
      state = INITIAL
      viewState.hidePasswordEditingDialog()
      return
    }
    this.password = password
    state = SAVE_PASSWORD_DIALOG
    viewState.showAcceptPasswordDialog()
  }
  
  fun acceptPassword() {
    state = LOADING
    viewState.showLoading()
    viewState.hideAcceptPasswordDialog()
    serviceInfo = ServiceInfo(serviceInfo.id, serviceInfo.name, serviceInfo.email, password)
    onIoThread {
      passwordsListRepository.updateServiceInfo(masterPassword, serviceInfo)
      state = INITIAL
      updateViewState {
        setPassword(password)
        hidePasswordEditingDialog()
        showFinishLoading()
      }
    }
  }
  
  fun closePasswordScreen() {
    state = INITIAL
    viewState.hidePasswordEditingDialog()
  }
  
  fun allowBackPress(): Boolean {
    return when (state) {
      INITIAL -> true
      LOADING, EDITING_NAME_OR_EMAIL -> false
      ERROR_EDITING_NAME -> {
        viewState.hideErrorSavingServiceName()
        state = EDITING_NAME_OR_EMAIL
        false
      }
      PASSWORD_EDITING_DIALOG -> {
        viewState.hidePasswordEditingDialog()
        state = INITIAL
        false
      }
      SAVE_PASSWORD_DIALOG -> {
        viewState.hideAcceptPasswordDialog()
        state = PASSWORD_EDITING_DIALOG
        false
      }
    }
  }
}