package com.arsvechkarev.vault.features.info

import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.model.ServiceInfo
import com.arsvechkarev.vault.cryptography.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.features.common.PasswordsListRepository

class InfoPresenter(
  private val passwordsListRepository: PasswordsListRepository,
  threader: Threader
) : BasePresenter<InfoView>(threader) {
  
  var isEditingNameOrEmailNow: Boolean = false
    private set
  
  var isEditingPasswordNow: Boolean = false
    private set
  
  private lateinit var serviceInfo: ServiceInfo
  
  fun performSetup(serviceInfo: ServiceInfo) {
    this.serviceInfo = serviceInfo
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
      viewState.showErrorSavingServiceName(serviceName)
      return false
    }
    return true
  }
  
  fun saveServiceName(serviceName: String) {
    isEditingNameOrEmailNow = false
    if (serviceInfo.name == serviceName) return
    viewState.showLoading()
    serviceInfo = ServiceInfo(serviceInfo.id, serviceName, serviceInfo.email, serviceInfo.password)
    viewState.showServiceName(serviceName)
    viewState.showLetterChange(serviceName[0].toString())
    onIoThread {
      passwordsListRepository.updateServiceInfo(masterPassword, serviceInfo)
      updateViewState { viewState.showFinishLoading() }
    }
  }
  
  fun saveEmail(email: String) {
    isEditingNameOrEmailNow = false
    if (serviceInfo.email == email) return
    viewState.showLoading()
    serviceInfo = ServiceInfo(serviceInfo.id, serviceInfo.name, email, serviceInfo.password)
    if (email.isBlank()) viewState.showNoEmail() else viewState.showEmail(email)
    onIoThread {
      passwordsListRepository.updateServiceInfo(masterPassword, serviceInfo)
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
    isEditingNameOrEmailNow = true
  }
  
  fun onEditPasswordIconClicked() {
    isEditingPasswordNow = true
    viewState.showPasswordEditingDialog(serviceInfo.password)
  }
  
  fun acceptNewPassword(password: String) {
  
  }
  
  fun allowBackPress(): Boolean {
    if (isEditingPasswordNow) {
      isEditingPasswordNow = false
      viewState.closePasswordDialog()
      return false
    }
    if (isEditingNameOrEmailNow) {
      viewState.switchFromEditingMode()
      return false
    }
    return true
  }
}