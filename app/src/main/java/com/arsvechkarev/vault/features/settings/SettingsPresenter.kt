package com.arsvechkarev.vault.features.settings

import android.annotation.SuppressLint
import buisnesslogic.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.core.Dispatchers
import com.arsvechkarev.vault.core.communicators.FlowCommunicator
import com.arsvechkarev.vault.core.di.FeatureScope
import com.arsvechkarev.vault.core.mvi.BaseMviPresenter
import com.arsvechkarev.vault.features.common.biometrics.BiometricsPrompt
import com.arsvechkarev.vault.features.common.biometrics.BiometricsPromptEvents.Error
import com.arsvechkarev.vault.features.common.biometrics.BiometricsPromptEvents.Failure
import com.arsvechkarev.vault.features.common.biometrics.BiometricsPromptEvents.Success
import com.arsvechkarev.vault.features.common.biometrics.BiometricsStorage
import com.arsvechkarev.vault.features.common.biometrics.SavedFingerprintChecker
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingActions.HideDialog
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingActions.ShowDialog
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingCommunicator
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingEvents
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingReactions.PasswordCheckedSuccessfully
import com.arsvechkarev.vault.features.settings.SettingsScreenActions.HidePasswordCheckingDialog
import com.arsvechkarev.vault.features.settings.SettingsScreenActions.ShowFingerprintEnteringEnabled
import com.arsvechkarev.vault.features.settings.SettingsScreenActions.ShowPasswordCheckingDialog
import com.arsvechkarev.vault.features.settings.SettingsScreenSingleEvents.ShowBiometricsAddedSuccessfully
import com.arsvechkarev.vault.features.settings.SettingsScreenUserActions.OnBackPressed
import com.arsvechkarev.vault.features.settings.SettingsScreenUserActions.OnUserFingerprintTextClicked
import com.arsvechkarev.vault.features.settings.SettingsScreenUserActions.ToggleUseFingerprintForEnteringCheckbox
import kotlinx.coroutines.launch
import navigation.Router
import timber.log.Timber
import javax.inject.Inject

@FeatureScope
class SettingsPresenter @Inject constructor(
  @PasswordCheckingCommunicator
  private val passwordCheckingCommunicator: FlowCommunicator<PasswordCheckingEvents>,
  private val fingerprintChecker: SavedFingerprintChecker,
  private val biometricsPrompt: BiometricsPrompt,
  private val biometricsStorage: BiometricsStorage,
  private val router: Router,
  dispatchers: Dispatchers,
) : BaseMviPresenter<SettingsScreenActions, SettingsScreenUserActions, SettingsScreenState>(
  SettingsScreenUserActions::class,
  dispatchers
) {
  
  init {
    subscribeToBiometricsEvents()
    subscribeToPasswordCheckingEvents()
    val fingerprintAuthAvailable = fingerprintChecker.isAuthorizationWithUserFingerAvailable()
    applyAction(ShowFingerprintEnteringEnabled(fingerprintAuthAvailable))
  }
  
  override fun getDefaultState(): SettingsScreenState {
    return SettingsScreenState()
  }
  
  override fun reduce(action: SettingsScreenActions) = when (action) {
    is ShowFingerprintEnteringEnabled -> {
      state.copy(fingerprintEnteringEnabled = action.enabled)
    }
    is ShowPasswordCheckingDialog -> {
      state.copy(showPasswordCheckingDialog = true)
    }
    is HidePasswordCheckingDialog -> {
      state.copy(showPasswordCheckingDialog = false)
    }
    else -> state
  }
  
  override fun onSideEffect(action: SettingsScreenUserActions) {
    when (action) {
      OnUserFingerprintTextClicked -> {
        onUseFingerprintsTextClicked()
      }
      is ToggleUseFingerprintForEnteringCheckbox -> {
        toggleUseFingerprintForEntering(action.enabled)
      }
      OnBackPressed -> {
        if (state.showPasswordCheckingDialog) {
          launch { passwordCheckingCommunicator.send(HideDialog) }
          applyAction(HidePasswordCheckingDialog)
        } else {
          router.goBack()
        }
      }
    }
  }
  
  private fun onUseFingerprintsTextClicked() {
    val isChecked = !fingerprintChecker.isAuthorizationWithUserFingerAvailable()
    reactToNewTogglingState(isChecked)
  }
  
  private fun toggleUseFingerprintForEntering(isChecked: Boolean) {
    val valueInPrefs = fingerprintChecker.isAuthorizationWithUserFingerAvailable()
    if (valueInPrefs != isChecked) {
      reactToNewTogglingState(isChecked)
    }
  }
  
  private fun reactToNewTogglingState(isChecked: Boolean) {
    launch {
      if (isChecked) {
        applyAction(ShowPasswordCheckingDialog)
        passwordCheckingCommunicator.send(ShowDialog)
        // Setting it to false temporarily, so that it doesn't show checked state before user entered password
        applyAction(ShowFingerprintEnteringEnabled(enabled = false))
      } else {
        onIoThread { biometricsStorage.deleteAllFiles() }
        fingerprintChecker.setAuthorizationWithUserFingerAvailable(false)
        applyAction(ShowFingerprintEnteringEnabled(enabled = false))
      }
    }
  }
  
  private fun subscribeToPasswordCheckingEvents() {
    passwordCheckingCommunicator.events.collectInPresenterScope { event ->
      when (event) {
        is PasswordCheckedSuccessfully -> {
          biometricsPrompt.showEncryptingBiometricsPrompt()
          applyAction(HidePasswordCheckingDialog)
          passwordCheckingCommunicator.send(HideDialog)
        }
      }
    }
  }
  
  @SuppressLint("NewApi")
  private fun subscribeToBiometricsEvents() {
    biometricsPrompt.biometricsEvents().collectInPresenterScope { event ->
      when (event) {
        is Success -> {
          onIoThread { biometricsStorage.encryptAndSaveData(masterPassword, event.result) }
          fingerprintChecker.setAuthorizationWithUserFingerAvailable(true)
          applyAction(ShowFingerprintEnteringEnabled(true))
          showSingleEvent(ShowBiometricsAddedSuccessfully)
        }
        is Error -> {
          Timber.d("Biometrics in SettingsPresenter error: ${event.errorString}")
        }
        is Failure -> {
          Timber.d("Biometrics in SettingsPresenter failed")
        }
      }
    }
  }
}