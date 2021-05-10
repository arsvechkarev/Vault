package com.arsvechkarev.vault.features.creating_service

import buisnesslogic.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Dispatchers
import com.arsvechkarev.vault.core.communicators.FlowCommunicator
import com.arsvechkarev.vault.core.model.ServiceModel
import com.arsvechkarev.vault.features.common.Screens
import com.arsvechkarev.vault.features.common.ServicesListenableRepository
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ConfigureMode.NewPassword
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ExitScreen
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ShowAcceptPasswordDialog
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ShowLoading
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingEvents
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingReactions.OnNewPasswordAccepted
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingReactions.OnSavePasswordButtonClicked
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingTag
import kotlinx.coroutines.launch
import navigation.Router
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named

class CreatingServicePresenter @Inject constructor(
  @Named(PasswordCreatingTag)
  private val passwordCreatingCommunicator: FlowCommunicator<PasswordCreatingEvents>,
  private val servicesRepository: ServicesListenableRepository,
  private val router: Router,
  dispatchers: Dispatchers
) : BasePresenter<CreatingServiceView>(dispatchers) {
  
  private var serviceName: String = ""
  private var username: String = ""
  private var email: String = ""
  
  init {
    subscribeToPasswordCreatingEvents()
  }
  
  fun onServiceNameChanged(text: String) {
    viewState.showServiceIcon(text)
  }
  
  fun onContinueClicked(serviceName: String, username: String, email: String) {
    if (serviceName.isBlank()) {
      viewState.showServiceNameCannotBeEmpty()
      return
    }
    this.serviceName = serviceName.trim()
    this.username = username.trim()
    this.email = email.trim()
    launch { passwordCreatingCommunicator.send(NewPassword) }
    router.goForward(Screens.PasswordCreatingScreen)
  }
  
  fun onBackClicked() {
    router.goBack()
  }
  
  private fun performServiceSaving(password: String) {
    launch {
      passwordCreatingCommunicator.send(ShowLoading)
      val serviceInfo = ServiceModel(UUID.randomUUID().toString(), serviceName,
        username, email, password)
      onIoThread { servicesRepository.saveService(masterPassword, serviceInfo) }
      passwordCreatingCommunicator.send(ExitScreen)
      router.goBackTo(Screens.ServicesListScreen)
    }
  }
  
  private fun subscribeToPasswordCreatingEvents() {
    passwordCreatingCommunicator.events.collectInPresenterScope { event ->
      when (event) {
        is OnSavePasswordButtonClicked -> {
          passwordCreatingCommunicator.send(ShowAcceptPasswordDialog)
        }
        is OnNewPasswordAccepted -> {
          performServiceSaving(event.password)
        }
      }
    }
  }
}