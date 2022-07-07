package com.arsvechkarev.vault.features.info

import buisnesslogic.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Clipboard
import com.arsvechkarev.vault.core.CoroutineRouter
import com.arsvechkarev.vault.core.DispatchersFacade
import com.arsvechkarev.vault.core.Screens
import com.arsvechkarev.vault.core.ServicesListenableRepository
import com.arsvechkarev.vault.core.communicators.FlowCommunicator
import com.arsvechkarev.vault.core.model.ServiceModel
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ConfigureMode.EditPassword
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ExitScreen
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ShowAcceptPasswordDialog
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingActions.ShowLoading
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingCommunicator
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingEvents
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingReactions.OnNewPasswordAccepted
import com.arsvechkarev.vault.features.creating_password.PasswordCreatingReactions.OnSavePasswordButtonClicked
import com.arsvechkarev.vault.features.info.InfoScreenState.DELETING_DIALOG
import com.arsvechkarev.vault.features.info.InfoScreenState.EDITING_NAME_OR_USERNAME_OR_EMAIL
import com.arsvechkarev.vault.features.info.InfoScreenState.INITIAL
import com.arsvechkarev.vault.features.info.InfoScreenState.LOADING
import kotlinx.coroutines.launch
import javax.inject.Inject

class InfoPresenter @Inject constructor(
  @PasswordCreatingCommunicator
  private val passwordCreatingCommunicator: FlowCommunicator<PasswordCreatingEvents>,
  private val servicesRepository: ServicesListenableRepository,
  private val clipboard: Clipboard,
  private val router: CoroutineRouter,
  dispatchers: DispatchersFacade
) : BasePresenter<InfoView>(dispatchers) {
  
  private lateinit var serviceModel: ServiceModel
  
  private var state = INITIAL
  
  val isEditingNameOrEmailNow get() = state == EDITING_NAME_OR_USERNAME_OR_EMAIL
  
  init {
    subscribeToPasswordCreatingEvents()
  }
  
  fun performSetup(serviceModel: ServiceModel) {
    this.serviceModel = serviceModel
    state = INITIAL
    viewState.showServiceName(serviceModel.serviceName)
    viewState.showServiceIcon(serviceModel.serviceName)
    viewState.setPassword(serviceModel.password)
    viewState.hidePassword()
    val username = serviceModel.username
    if (username.isEmpty()) viewState.showNoUsername() else viewState.showUsername(username)
    val email = serviceModel.email
    if (email.isEmpty()) viewState.showNoEmail() else viewState.showEmail(email)
  }
  
  fun onServiceNameChanged(text: String) {
    viewState.showServiceIcon(text)
  }
  
  fun saveServiceName(serviceName: String) {
    state = INITIAL
    if (serviceModel.serviceName == serviceName) return
    viewState.showLoading()
    state = LOADING
    serviceModel = serviceModel.copy(serviceName = serviceName)
    viewState.showServiceName(serviceName)
    viewState.showServiceIcon(serviceName)
    launch {
      onIoThread { servicesRepository.updateService(masterPassword, serviceModel) }
      state = INITIAL
      viewState.hideLoading()
    }
  }
  
  fun saveUsername(username: String) {
    state = INITIAL
    if (serviceModel.username == username) return
    viewState.showLoading()
    state = LOADING
    serviceModel = serviceModel.copy(username = username)
    if (username.isBlank()) viewState.showNoUsername() else viewState.showUsername(username)
    launch {
      onIoThread { servicesRepository.updateService(masterPassword, serviceModel) }
      state = INITIAL
      viewState.hideLoading()
    }
  }
  
  fun saveEmail(email: String) {
    state = INITIAL
    if (serviceModel.email == email) return
    viewState.showLoading()
    state = LOADING
    serviceModel = serviceModel.copy(email = email)
    if (email.isBlank()) viewState.showNoEmail() else viewState.showEmail(email)
    launch {
      onIoThread { servicesRepository.updateService(masterPassword, serviceModel) }
      state = INITIAL
      viewState.hideLoading()
    }
  }
  
  fun onTogglePassword(isPasswordShown: Boolean) {
    if (isPasswordShown) {
      viewState.showPassword(serviceModel.password)
    } else {
      viewState.hidePassword()
    }
  }
  
  fun switchToEditingMode() {
    state = EDITING_NAME_OR_USERNAME_OR_EMAIL
  }
  
  fun onDeleteClicked() {
    state = DELETING_DIALOG
    viewState.showDeleteDialog(serviceModel.serviceName)
  }
  
  fun onHideDeleteDialog() {
    state = INITIAL
    viewState.hideDeleteDialog()
  }
  
  fun agreeToDeleteService() {
    viewState.hideDeleteDialog()
    viewState.showLoading()
    launch {
      onIoThread {
        servicesRepository.deleteService(masterPassword, serviceModel, notifySubscribers = true)
      }
      state = INITIAL
      viewState.showExit()
      router.goBack(releaseCurrentScreen = false)
    }
  }
  
  fun onCopyClicked() {
    clipboard.copyToClipboard(serviceModel.password)
    viewState.showCopiedPassword()
  }
  
  fun onEditPasswordIconClicked() {
    router.goForward(Screens.PasswordCreatingScreen)
    launch { passwordCreatingCommunicator.send(EditPassword(serviceModel.password)) }
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
  
  private fun subscribeToPasswordCreatingEvents() {
    passwordCreatingCommunicator.events.collectInPresenterScope { event ->
      when (event) {
        is OnSavePasswordButtonClicked -> reactToSaveButtonClicked(event.password)
        is OnNewPasswordAccepted -> reactToNewPasswordSaved(event.password)
      }
    }
  }
  
  private fun reactToSaveButtonClicked(newPassword: String) {
    if (serviceModel.password == newPassword) {
      // Password is the same as was before, just closing PasswordCreatingScreen
      router.goBack()
      return
    }
    launch { passwordCreatingCommunicator.send(ShowAcceptPasswordDialog) }
  }
  
  private fun reactToNewPasswordSaved(newPassword: String) {
    launch {
      passwordCreatingCommunicator.send(ShowLoading)
      serviceModel = serviceModel.copy(password = newPassword)
      onIoThread { servicesRepository.updateService(masterPassword, serviceModel) }
      viewState.setPassword(newPassword)
      passwordCreatingCommunicator.send(ExitScreen)
      router.goBack()
    }
  }
}
