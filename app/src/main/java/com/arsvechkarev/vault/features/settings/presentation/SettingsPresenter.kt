package com.arsvechkarev.vault.features.settings.presentation

import android.annotation.SuppressLint
import buisnesslogic.MasterPasswordHolder.masterPassword
import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Dispatchers
import com.arsvechkarev.vault.core.communicators.FlowCommunicator
import com.arsvechkarev.vault.core.di.FeatureScope
import com.arsvechkarev.vault.features.common.biometrics.BiometricsPrompt
import com.arsvechkarev.vault.features.common.biometrics.BiometricsPromptEvents.Error
import com.arsvechkarev.vault.features.common.biometrics.BiometricsPromptEvents.Failure
import com.arsvechkarev.vault.features.common.biometrics.BiometricsPromptEvents.Success
import com.arsvechkarev.vault.features.common.biometrics.BiometricsStorage
import com.arsvechkarev.vault.features.common.biometrics.SavedFingerprintChecker
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingActions
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingEvents
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingReactions.PasswordCheckedSuccessfully
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingTag
import kotlinx.coroutines.launch
import navigation.Router
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@FeatureScope
class SettingsPresenter @Inject constructor(
  @Named(PasswordCheckingTag)
  private val passwordCheckingCommunicator: FlowCommunicator<PasswordCheckingEvents>,
  private val fingerprintChecker: SavedFingerprintChecker,
  private val biometricsPrompt: BiometricsPrompt,
  private val biometricsStorage: BiometricsStorage,
  private val router: Router,
  dispatchers: Dispatchers,
) : BasePresenter<SettingsView>(dispatchers) {
  
  init {
    subscribeToBiometricsEvents()
    subscribeToPasswordCheckingEvents()
  }
  
  override fun onFirstViewAttach() {
    viewState.showUseFingerprintForEnteringEnabled(
      fingerprintChecker.isAuthorizationWithUserFingerAvailable()
    )
  }
  
  fun onUseFingerprintsTextClicked() {
    val isChecked = !fingerprintChecker.isAuthorizationWithUserFingerAvailable()
    reactToNewTogglingState(isChecked)
  }
  
  fun toggleUseFingerprintForEntering(isChecked: Boolean) {
    val valueInPrefs = fingerprintChecker.isAuthorizationWithUserFingerAvailable()
    if (valueInPrefs != isChecked) {
      reactToNewTogglingState(isChecked)
    }
  }
  
  fun onBackClicked() {
    router.goBack()
  }
  
  private fun reactToNewTogglingState(isChecked: Boolean) {
    launch {
      if (isChecked) {
        passwordCheckingCommunicator.send(PasswordCheckingActions.ShowDialog)
        // Setting it to false temporarily, so that it doesn't show checked state before user entered password
        viewState.showUseFingerprintForEnteringEnabled(false)
      } else {
        onIoThread { biometricsStorage.deleteAllFiles() }
        fingerprintChecker.setAuthorizationWithUserFingerAvailable(false)
        viewState.showUseFingerprintForEnteringEnabled(false)
      }
    }
  }
  
  private fun subscribeToPasswordCheckingEvents() {
    passwordCheckingCommunicator.events.collectInPresenterScope { event ->
      when (event) {
        is PasswordCheckedSuccessfully -> {
          biometricsPrompt.showEncryptingBiometricsPrompt()
          passwordCheckingCommunicator.send(PasswordCheckingActions.HideDialog)
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
          viewState.showUseFingerprintForEnteringEnabled(true)
          viewState.showAddedBiometricsSuccessfully()
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