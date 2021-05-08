package com.arsvechkarev.vault.features.info

import com.arsvechkarev.vault.core.BasePresenterWithChannels
import com.arsvechkarev.vault.core.Clipboard
import com.arsvechkarev.vault.core.Screens
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.channels.Communicator
import com.arsvechkarev.vault.core.di.FeatureScope
import com.arsvechkarev.vault.core.model.Service
import com.arsvechkarev.vault.cryptography.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.features.common.ServicesRepository
import com.arsvechkarev.vault.features.common.getIconForServiceName
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ConfigureMode.EditPassword
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ExitScreen
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ShowAcceptPasswordDialog
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ShowLoading
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingEvents
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingReactions.OnNewPasswordAccepted
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingReactions.OnSavePasswordButtonClicked
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingTag
import com.arsvechkarev.vault.features.info.InfoScreenState.DELETING_DIALOG
import com.arsvechkarev.vault.features.info.InfoScreenState.EDITING_NAME_OR_USERNAME_OR_EMAIL
import com.arsvechkarev.vault.features.info.InfoScreenState.INITIAL
import com.arsvechkarev.vault.features.info.InfoScreenState.LOADING
import navigation.Router
import javax.inject.Inject
import javax.inject.Named

@FeatureScope
class InfoPresenter @Inject constructor(
  private val servicesRepository: ServicesRepository,
  private val clipboard: Clipboard,
  @Named(
    PasswordCreatingTag) private val passwordCreatingCommunicator: Communicator<PasswordCreatingEvents>,
  private val router: Router,
  threader: Threader
) : BasePresenterWithChannels<InfoView>(threader) {
  
  private lateinit var service: Service
  
  private var state = INITIAL
  
  val isEditingNameOrEmailNow get() = state == EDITING_NAME_OR_USERNAME_OR_EMAIL
  
  init {
    subscribeToChannel(passwordCreatingCommunicator) { event ->
      when (event) {
        is OnSavePasswordButtonClicked -> reactToSaveButtonClicked(event.password)
        is OnNewPasswordAccepted -> reactToNewPasswordSaved(event.password)
      }
    }
  }
  
  fun performSetup(service: Service) {
    this.service = service
    state = INITIAL
    updateServiceIcon(service.serviceName)
    viewState.showServiceName(service.serviceName)
    viewState.setPassword(service.password)
    viewState.hidePassword()
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
    service = service.copy(serviceName = serviceName)
    viewState.showServiceName(serviceName)
    updateServiceIcon(serviceName)
    onIoThread {
      servicesRepository.updateService(masterPassword, service)
      state = INITIAL
      onMainThread { viewState.hideLoading() }
    }
  }
  
  fun saveUsername(username: String) {
    state = INITIAL
    if (service.username == username) return
    viewState.showLoading()
    state = LOADING
    service = service.copy(username = username)
    if (username.isBlank()) viewState.showNoUsername() else viewState.showUsername(username)
    onIoThread {
      servicesRepository.updateService(masterPassword, service)
      state = INITIAL
      onMainThread { viewState.hideLoading() }
    }
  }
  
  fun saveEmail(email: String) {
    state = INITIAL
    if (service.email == email) return
    viewState.showLoading()
    state = LOADING
    service = service.copy(email = email)
    if (email.isBlank()) viewState.showNoEmail() else viewState.showEmail(email)
    onIoThread {
      servicesRepository.updateService(masterPassword, service)
      state = INITIAL
      onMainThread { viewState.hideLoading() }
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
      onMainThread {
        viewState.showExit()
        router.goBack(releaseCurrentScreen = false)
      }
    }
  }
  
  fun onCopyClicked() {
    clipboard.copyPassword(service.password)
    viewState.showCopiedPassword()
  }
  
  fun onEditPasswordIconClicked() {
    router.goForward(Screens.PasswordCreatingScreen)
    passwordCreatingCommunicator.send(EditPassword(service.password))
  }
  
  fun onBackClicked() {
    if (!handleBackPress()) router.goBack(releaseCurrentScreen = false)
  }
  
  fun handleBackPress(): Boolean {
    return when (state) {
      INITIAL -> false
      LOADING -> true
      EDITING_NAME_OR_USERNAME_OR_EMAIL -> {
        viewState.restoreInitialData()
        state = INITIAL
        true
      }
      DELETING_DIALOG -> {
        viewState.hideDeleteDialog()
        state = INITIAL
        true
      }
    }
  }
  
  private fun reactToSaveButtonClicked(newPassword: String) {
    if (service.password == newPassword) {
      // Password is the same as was before, just closing PasswordCreatingScreen
      router.goBack()
      return
    }
    passwordCreatingCommunicator.send(ShowAcceptPasswordDialog)
  }
  
  private fun reactToNewPasswordSaved(newPassword: String) {
    passwordCreatingCommunicator.send(ShowLoading)
    service = service.copy(password = newPassword)
    onIoThread {
      servicesRepository.updateService(masterPassword, service)
      onMainThread {
        viewState.setPassword(newPassword)
        passwordCreatingCommunicator.send(ExitScreen)
        router.goBack()
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