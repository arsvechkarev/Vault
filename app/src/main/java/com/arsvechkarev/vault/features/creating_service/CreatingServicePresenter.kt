package com.arsvechkarev.vault.features.creating_service

import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.model.ServiceInfo
import com.arsvechkarev.vault.cryptography.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.features.common.PasswordsListCachingRepository
import java.util.UUID

class CreatingServicePresenter(
  private val passwordsListCachingRepository: PasswordsListCachingRepository,
  threader: Threader
) : BasePresenter<CreatingServiceView>(threader) {
  
  private var serviceName: String = ""
  private var email: String = ""
  
  fun onContinueClicked(serviceName: String, email: String) {
    val passwords = passwordsListCachingRepository.getAllServicesInfo(masterPassword)
    for (serviceInfo in passwords) {
      if (serviceInfo.name == serviceName) {
        viewState.showServiceNameAlreadyExists()
        break
      }
    }
    this.serviceName = serviceName.trim()
    this.email = email.trim()
    viewState.showPasswordCreatingDialog()
  }
  
  fun onPasswordEntered(password: String) {
    viewState.showLoadingCreation()
    onIoThread {
      val serviceInfo = ServiceInfo(UUID.randomUUID().toString(), serviceName, email, password)
      passwordsListCachingRepository.saveServiceInfo(masterPassword, serviceInfo)
      updateViewState { viewState.showServiceInfoCreated() }
    }
  }
}