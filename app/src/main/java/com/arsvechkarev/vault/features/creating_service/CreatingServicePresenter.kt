package com.arsvechkarev.vault.features.creating_service

import buisnesslogic.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.core.BasePresenterWithChannels
import com.arsvechkarev.vault.core.Screens
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.communicators.Communicator
import com.arsvechkarev.vault.core.model.ServiceModel
import com.arsvechkarev.vault.features.common.ServicesRepository
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ConfigureMode.NewPassword
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ExitScreen
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ShowAcceptPasswordDialog
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ShowLoading
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingEvents
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingReactions.OnNewPasswordAccepted
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingReactions.OnSavePasswordButtonClicked
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingTag
import navigation.Router
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named

class CreatingServicePresenter @Inject constructor(
  @Named(PasswordCreatingTag)
  private val passwordCreatingCommunicator: Communicator<PasswordCreatingEvents>,
  private val servicesRepository: ServicesRepository,
  private val router: Router,
  threader: Threader
) : BasePresenterWithChannels<CreatingServiceView>(threader) {
  
  private var serviceName: String = ""
  private var username: String = ""
  private var email: String = ""
  
  init {
    subscribeToChannel(passwordCreatingCommunicator) { event ->
      when (event) {
        is OnSavePasswordButtonClicked -> passwordCreatingCommunicator.send(
          ShowAcceptPasswordDialog)
        is OnNewPasswordAccepted -> performServiceSaving(event.password)
      }
    }
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
    passwordCreatingCommunicator.send(NewPassword)
    router.goForward(Screens.PasswordCreatingScreen)
  }
  
  fun onBackClicked() {
    router.goBack()
  }
  
  private fun performServiceSaving(password: String) {
    passwordCreatingCommunicator.send(ShowLoading)
    onIoThread {
      val serviceInfo = ServiceModel(UUID.randomUUID().toString(), serviceName,
        username, email, password)
      servicesRepository.saveService(masterPassword, serviceInfo)
      onMainThread {
        passwordCreatingCommunicator.send(ExitScreen)
        router.goBackTo(Screens.ServicesListScreen)
      }
    }
  }
}