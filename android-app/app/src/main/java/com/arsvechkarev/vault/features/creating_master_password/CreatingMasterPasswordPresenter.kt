package com.arsvechkarev.vault.features.creating_master_password

import buisnesslogic.MasterPasswordChecker
import buisnesslogic.MasterPasswordHolder
import buisnesslogic.PasswordChecker
import buisnesslogic.PasswordStatus.OK
import com.arsvechkarev.vault.common.Screens
import com.arsvechkarev.vault.common.UserAuthSaver
import com.arsvechkarev.vault.core.Dispatchers
import com.arsvechkarev.vault.core.di.FeatureScope
import com.arsvechkarev.vault.core.mvi.BaseMviPresenter
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreenActions.PasswordEnteringStateChanged
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreenActions.ShowPasswordsDontMatch
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreenActions.ShowPasswordsMatch
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreenActions.UpdatePasswordStatus
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreenActions.UpdatePasswordStrength
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreenUserActions.OnBackButtonClicked
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreenUserActions.OnBackPressed
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreenUserActions.OnContinueClicked
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreenUserActions.OnInitialPasswordTyping
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreenUserActions.OnRepeatPasswordTyping
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreenUserActions.RequestHidePasswordStrengthDialog
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreenUserActions.RequestShowPasswordStrengthDialog
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordSingleEvents.FinishingAuthorization
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordSingleEvents.HideErrorText
import com.arsvechkarev.vault.features.creating_master_password.PasswordEnteringState.INITIAL
import com.arsvechkarev.vault.features.creating_master_password.PasswordEnteringState.REPEATING
import kotlinx.coroutines.launch
import navigation.Router
import javax.inject.Inject

typealias Actions = CreatingMasterPasswordScreenActions
typealias UserActions = CreatingMasterPasswordScreenUserActions
typealias State = CreatingMasterPasswordScreenState

@FeatureScope
class CreatingMasterPasswordPresenter @Inject constructor(
  private val passwordChecker: PasswordChecker,
  private val masterPasswordChecker: MasterPasswordChecker,
  private val userAuthSaver: UserAuthSaver,
  private val router: Router,
  dispatchers: Dispatchers
) : BaseMviPresenter<Actions, UserActions, State>(UserActions::class, dispatchers) {
  
  override fun getDefaultState(): State {
    return State()
  }
  
  override fun reduce(action: Actions) = when (action) {
    is OnInitialPasswordTyping -> state.copy(initialPassword = action.password)
    is OnRepeatPasswordTyping -> state.copy(repeatedPassword = action.password)
    is UpdatePasswordStatus -> state.copy(passwordStatus = action.passwordStatus)
    is UpdatePasswordStrength -> state.copy(passwordStrength = action.passwordStrength)
    is PasswordEnteringStateChanged -> state.copy(passwordEnteringState = action.state)
    is ShowPasswordsMatch -> state.copy(passwordsMatch = true)
    is ShowPasswordsDontMatch -> state.copy(passwordsMatch = false)
    RequestShowPasswordStrengthDialog -> state.copy(showPasswordStrengthDialog = true)
    RequestHidePasswordStrengthDialog -> state.copy(showPasswordStrengthDialog = false)
    else -> state
  }
  
  override fun onSideEffect(action: UserActions) {
    when (action) {
      is OnInitialPasswordTyping -> {
        val strength = passwordChecker.checkStrength(action.password)
        applyAction(UpdatePasswordStrength(strength))
        showSingleEvent(HideErrorText)
      }
      is OnRepeatPasswordTyping -> {
        showSingleEvent(HideErrorText)
      }
      OnBackPressed, OnBackButtonClicked -> {
        if (state.showPasswordStrengthDialog) {
          applyAction(RequestHidePasswordStrengthDialog)
        } else {
          when (state.passwordEnteringState) {
            INITIAL -> router.goBack()
            REPEATING -> applyAction(PasswordEnteringStateChanged(INITIAL))
          }
        }
      }
      OnContinueClicked -> {
        when (state.passwordEnteringState) {
          INITIAL -> onEnteredPassword(state.initialPassword)
          REPEATING -> onRepeatedPassword(state.repeatedPassword)
        }
      }
      else -> Unit
    }
  }
  
  private fun onEnteredPassword(password: String) {
    val passwordStatus = passwordChecker.validate(password)
    applyAction(UpdatePasswordStatus(passwordStatus))
    if (passwordStatus == OK) {
      applyAction(PasswordEnteringStateChanged(REPEATING))
    }
  }
  
  private fun onRepeatedPassword(password: String) {
    if (state.initialPassword != "" && password == state.initialPassword) {
      applyAction(ShowPasswordsMatch)
      finishAuthorization()
    } else {
      applyAction(ShowPasswordsDontMatch)
    }
  }
  
  private fun finishAuthorization() {
    showSingleEvent(FinishingAuthorization)
    launch {
      onBackgroundThread {
        userAuthSaver.setUserIsAuthorized(true)
        masterPasswordChecker.initializeEncryptedFile(state.initialPassword)
        MasterPasswordHolder.setMasterPassword(state.initialPassword)
      }
      router.switchToNewRoot(Screens.ServicesListScreen)
    }
  }
}
