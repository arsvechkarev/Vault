package com.arsvechkarev.vault.features.info

import buisnesslogic.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.CachedPasswordsStorage
import com.arsvechkarev.vault.core.Clipboard
import com.arsvechkarev.vault.core.DispatchersFacade
import com.arsvechkarev.vault.core.Router
import com.arsvechkarev.vault.core.Screens
import com.arsvechkarev.vault.core.communicators.FlowCommunicator
import com.arsvechkarev.vault.core.model.PasswordInfoItem
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

class InfoPresenter constructor(
  @PasswordCreatingCommunicator
  private val passwordCreatingCommunicator: FlowCommunicator<PasswordCreatingEvents>,
  private val servicesRepository: CachedPasswordsStorage,
  private val clipboard: Clipboard,
  private val router: Router,
  dispatchers: DispatchersFacade
) : BasePresenter<InfoView>(dispatchers) {
  
  private lateinit var passwordInfoItem: PasswordInfoItem
  
  private var state = INITIAL
  
  val isEditingNameOrEmailNow get() = state == EDITING_NAME_OR_USERNAME_OR_EMAIL
  
  init {
    subscribeToPasswordCreatingEvents()
  }
  
  fun performSetup(passwordInfoItem: PasswordInfoItem) {
    this.passwordInfoItem = passwordInfoItem
    state = INITIAL
    viewState.showServiceName(passwordInfoItem.websiteName)
    viewState.showServiceIcon(passwordInfoItem.websiteName)
    viewState.setPassword(passwordInfoItem.notes)
    viewState.hidePassword()
    val username = passwordInfoItem.login
    if (username.isEmpty()) viewState.showNoUsername() else viewState.showUsername(username)
    val email = passwordInfoItem.password
    if (email.isEmpty()) viewState.showNoEmail() else viewState.showEmail(email)
  }
  
  fun onServiceNameChanged(text: String) {
    viewState.showServiceIcon(text)
  }
  
  fun saveServiceName(serviceName: String) {
    state = INITIAL
    if (passwordInfoItem.websiteName == serviceName) return
    viewState.showLoading()
    state = LOADING
    passwordInfoItem = passwordInfoItem.copy(websiteName = serviceName)
    viewState.showServiceName(serviceName)
    viewState.showServiceIcon(serviceName)
    launch {
      onIoThread { servicesRepository.updatePassword(masterPassword, passwordInfoItem) }
      state = INITIAL
      viewState.hideLoading()
    }
  }
  
  fun saveUsername(username: String) {
    state = INITIAL
    if (passwordInfoItem.login == username) return
    viewState.showLoading()
    state = LOADING
    passwordInfoItem = passwordInfoItem.copy(login = username)
    if (username.isBlank()) viewState.showNoUsername() else viewState.showUsername(username)
    launch {
      onIoThread { servicesRepository.updatePassword(masterPassword, passwordInfoItem) }
      state = INITIAL
      viewState.hideLoading()
    }
  }
  
  fun saveEmail(email: String) {
    state = INITIAL
    if (passwordInfoItem.password == email) return
    viewState.showLoading()
    state = LOADING
    passwordInfoItem = passwordInfoItem.copy(password = email)
    if (email.isBlank()) viewState.showNoEmail() else viewState.showEmail(email)
    launch {
      onIoThread { servicesRepository.updatePassword(masterPassword, passwordInfoItem) }
      state = INITIAL
      viewState.hideLoading()
    }
  }
  
  fun onTogglePassword(isPasswordShown: Boolean) {
    if (isPasswordShown) {
      viewState.showPassword(passwordInfoItem.notes)
    } else {
      viewState.hidePassword()
    }
  }
  
  fun switchToEditingMode() {
    state = EDITING_NAME_OR_USERNAME_OR_EMAIL
  }
  
  fun onDeleteClicked() {
    state = DELETING_DIALOG
    viewState.showDeleteDialog(passwordInfoItem.websiteName)
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
        servicesRepository.deletePassword(masterPassword, passwordInfoItem)
      }
      state = INITIAL
      viewState.showExit()
      router.goBack(releaseCurrentScreen = false)
    }
  }
  
  fun onCopyClicked() {
    clipboard.copyToClipboard(passwordInfoItem.notes)
    viewState.showCopiedPassword()
  }
  
  fun onEditPasswordIconClicked() {
    router.goForward(Screens.PasswordCreatingScreen)
    launch { passwordCreatingCommunicator.send(EditPassword(passwordInfoItem.notes)) }
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
    if (passwordInfoItem.notes == newPassword) {
      // Password is the same as was before, just closing PasswordCreatingScreen
      router.goBack()
      return
    }
    launch { passwordCreatingCommunicator.send(ShowAcceptPasswordDialog) }
  }
  
  private fun reactToNewPasswordSaved(newPassword: String) {
    launch {
      passwordCreatingCommunicator.send(ShowLoading)
      passwordInfoItem = passwordInfoItem.copy(notes = newPassword)
      onIoThread { servicesRepository.updatePassword(masterPassword, passwordInfoItem) }
      viewState.setPassword(newPassword)
      passwordCreatingCommunicator.send(ExitScreen)
      router.goBack()
    }
  }
}
