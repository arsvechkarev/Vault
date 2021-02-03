package com.arsvechkarev.vault.features.info

import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.model.ServiceInfo
import com.arsvechkarev.vault.cryptography.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.features.common.PasswordsListCachingRepository
import timber.log.Timber

class InfoPresenter(
  private val passwordsListCachingRepository: PasswordsListCachingRepository,
  threader: Threader
) : BasePresenter<InfoView>(threader) {
  
  var isEditingSomethingNow: Boolean = false
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
  
  fun onEditPasswordIconClicked() {
    viewState.showPasswordEditingDialog()
  }
  
  fun onServiceNameSavingAllowed(serviceName: String): Boolean {
    if (serviceName == serviceInfo.name) return true
    val servicesInfo = passwordsListCachingRepository.getAllServicesInfo(masterPassword)
    if (servicesInfo.find { it.name == serviceName } != null) {
      viewState.showErrorSavingServiceName(serviceName)
      return false
    }
    return true
  }
  
  fun saveServiceName(serviceName: String) {
    isEditingSomethingNow = false
    if (serviceInfo.name == serviceName) return
    viewState.showLoading()
    serviceInfo = ServiceInfo(serviceInfo.id, serviceName, serviceInfo.email, serviceInfo.password)
    viewState.showServiceName(serviceName)
    viewState.showLetterChange(serviceName[0].toString())
    onIoThread {
      passwordsListCachingRepository.updateServiceInfo(masterPassword, serviceInfo)
      onMainThread {
        viewState.showFinishLoading()
      }
    }
  }
  
  fun saveEmail(email: String) {
    isEditingSomethingNow = false
    viewState.showLoading()
    serviceInfo = ServiceInfo(serviceInfo.id, serviceInfo.name, email, serviceInfo.password)
    viewState.showEmail(email)
    onIoThread {
      passwordsListCachingRepository.updateServiceInfo(masterPassword, serviceInfo)
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
    isEditingSomethingNow = true
  }
  
  fun onEditPasswordClicked() {
  }
  
  fun allowBackPress(): Boolean {
    if (isEditingSomethingNow) {
      viewState.switchFromEditingMode()
      return false
    }
    return true
  }
}