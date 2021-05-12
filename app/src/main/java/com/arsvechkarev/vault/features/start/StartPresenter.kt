package com.arsvechkarev.vault.features.start

import android.annotation.SuppressLint
import buisnesslogic.MasterPasswordChecker
import buisnesslogic.MasterPasswordHolder
import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Dispatchers
import com.arsvechkarev.vault.core.di.FeatureScope
import com.arsvechkarev.vault.features.common.Screens
import com.arsvechkarev.vault.features.common.biometrics.BiometricsPrompt
import com.arsvechkarev.vault.features.common.biometrics.BiometricsPromptEvents
import com.arsvechkarev.vault.features.common.biometrics.BiometricsStorage
import com.arsvechkarev.vault.features.common.biometrics.SavedFingerprintChecker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import navigation.Router
import timber.log.Timber
import javax.inject.Inject

@FeatureScope
class StartPresenter @Inject constructor(
  private val masterPasswordChecker: MasterPasswordChecker,
  private val fingerprintChecker: SavedFingerprintChecker,
  private val biometricsPrompt: BiometricsPrompt,
  private val biometricsStorage: BiometricsStorage,
  private val router: Router,
  dispatchers: Dispatchers,
) : BasePresenter<StartView>(dispatchers) {
  
  init {
    subscribeToBiometricsEvents()
  }
  
  @SuppressLint("NewApi")
  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    if (fingerprintChecker.isAuthorizationWithUserFingerAvailable()) {
      showBiometricsPrompt()
    } else {
      viewState.showKeyboard()
    }
  }
  
  fun onEnteredPassword(password: String) {
    if (password.isBlank()) return
    viewState.showLoadingCheckingPassword()
    launch {
      val isCorrect = onIoThread { masterPasswordChecker.isCorrect(password) }
      if (isCorrect) {
        MasterPasswordHolder.setMasterPassword(password)
        viewState.showSuccessCheckingPassword()
        router.switchToNewRoot(Screens.ServicesListScreen)
      } else {
        viewState.showFailureCheckingPassword()
      }
    }
  }
  
  fun onFingerprintIconClicked() {
    showBiometricsPrompt()
  }
  
  private fun showBiometricsPrompt() {
    launch {
      val iv = onIoThread { biometricsStorage.getIv() }
      biometricsPrompt.showDecryptingBiometricsPrompt(iv)
    }
  }
  
  @SuppressLint("NewApi")
  private fun subscribeToBiometricsEvents() {
    launch {
      biometricsPrompt.biometricsEvents().collect { event ->
        when (event) {
          is BiometricsPromptEvents.Success -> {
            val masterPassword = onIoThread { biometricsStorage.getMasterPassword(event.result) }
            require(masterPassword.isNotEmpty())
            onEnteredPassword(masterPassword)
            viewState.showStubPasswordInEditText()
          }
          is BiometricsPromptEvents.Lockout -> {
            viewState.showTooManyAttemptsTryAgainLater()
          }
          is BiometricsPromptEvents.LockoutPermanent -> {
            viewState.showPermanentLockout()
            viewState.hideFingerprintIcon()
          }
          is BiometricsPromptEvents.Error -> {
            viewState.showFingerprintIcon()
            viewState.showKeyboard()
            Timber.d("Biometrics in StartPresenter error: ${event.errorString}")
          }
          is BiometricsPromptEvents.Failure -> {
            Timber.d("Biometrics in StartPresenter failed")
          }
        }
      }
    }
  }
}