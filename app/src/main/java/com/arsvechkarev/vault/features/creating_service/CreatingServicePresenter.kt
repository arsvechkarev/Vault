package com.arsvechkarev.vault.features.creating_service

import com.arsvechkarev.vault.core.BasePresenterWithChannels
import com.arsvechkarev.vault.core.Screens
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.channels.Channel
import com.arsvechkarev.vault.core.model.Service
import com.arsvechkarev.vault.cryptography.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.features.common.ServicesRepository
import com.arsvechkarev.vault.features.common.getIconForServiceName
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
  private val servicesRepository: ServicesRepository,
  @Named(PasswordCreatingTag) private val passwordCreatingChannel: Channel<PasswordCreatingEvents>,
  private val router: Router,
  threader: Threader
) : BasePresenterWithChannels<CreatingServiceView>(threader) {
  
  private var serviceName: String = ""
  private var username: String = ""
  private var email: String = ""
  
  init {
    subscribeToChannel(passwordCreatingChannel) { event ->
      when (event) {
        is OnSavePasswordButtonClicked -> passwordCreatingChannel.send(ShowAcceptPasswordDialog)
        is OnNewPasswordAccepted -> performServiceSaving(event.password)
      }
    }
  }
  
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
    this.serviceName = serviceName.trim()
    this.username = username.trim()
    this.email = email.trim()
    passwordCreatingChannel.send(NewPassword)
    router.goForward(Screens.PasswordCreatingScreen)
  }
  
  fun onBackClicked() {
    router.goBack()
  }
  
  private fun performServiceSaving(password: String) {
    viewState.showLoadingCreation()
    passwordCreatingChannel.send(ShowLoading)
    onIoThread {
      val serviceInfo = Service(UUID.randomUUID().toString(), serviceName,
        username, email, password)
      servicesRepository.saveService(masterPassword, serviceInfo)
      onMainThread {
        passwordCreatingChannel.send(ExitScreen)
        router.goBackTo(Screens.ServicesListScreen)
      }
    }
  }
}