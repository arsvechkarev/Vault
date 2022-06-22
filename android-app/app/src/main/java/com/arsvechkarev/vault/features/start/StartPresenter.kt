package com.arsvechkarev.vault.features.start

import android.annotation.SuppressLint
import buisnesslogic.MasterPasswordChecker
import buisnesslogic.MasterPasswordHolder
import com.arsvechkarev.vault.common.Screens
import com.arsvechkarev.vault.common.biometrics.BiometricsPrompt
import com.arsvechkarev.vault.common.biometrics.BiometricsPromptEvents
import com.arsvechkarev.vault.common.biometrics.BiometricsStorage
import com.arsvechkarev.vault.common.biometrics.SavedFingerprintChecker
import com.arsvechkarev.vault.core.Dispatchers
import com.arsvechkarev.vault.core.di.FeatureScope
import com.arsvechkarev.vault.core.mvi.BaseMviPresenter
import com.arsvechkarev.vault.features.start.StartScreenAction.HideFingerprintIcon
import com.arsvechkarev.vault.features.start.StartScreenAction.ShowBiometricsError
import com.arsvechkarev.vault.features.start.StartScreenAction.ShowFailureCheckingPassword
import com.arsvechkarev.vault.features.start.StartScreenAction.ShowFingerprintIcon
import com.arsvechkarev.vault.features.start.StartScreenAction.ShowKeyboard
import com.arsvechkarev.vault.features.start.StartScreenAction.ShowLoadingCheckingPassword
import com.arsvechkarev.vault.features.start.StartScreenAction.ShowSuccessCheckingPassword
import com.arsvechkarev.vault.features.start.StartScreenSingleEvent.ShowEditTextStubPassword
import com.arsvechkarev.vault.features.start.StartScreenSingleEvent.ShowPermanentLockout
import com.arsvechkarev.vault.features.start.StartScreenSingleEvent.ShowTooManyAttemptsTryAgainLater
import com.arsvechkarev.vault.features.start.StartScreenUserAction.OnEditTextTyping
import com.arsvechkarev.vault.features.start.StartScreenUserAction.OnEnteredPassword
import com.arsvechkarev.vault.features.start.StartScreenUserAction.OnFingerprintIconClicked
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
) : BaseMviPresenter<StartScreenAction, StartScreenUserAction, StartScreenState>(
  StartScreenUserAction::class,
  dispatchers
) {
  
  init {
    subscribeToBiometricsEvents()
  }
  
  @SuppressLint("NewApi")
  override fun onFirstViewAttach() {
    if (fingerprintChecker.isAuthorizationWithUserFingerAvailable()) {
      showBiometricsPrompt()
    } else {
      applyAction(ShowKeyboard)
    }
  }
  
  override fun getDefaultState(): StartScreenState {
    return StartScreenState()
  }
  
  override fun reduce(action: StartScreenAction) = when (action) {
    ShowFingerprintIcon -> state.copy(showFingerprintIcon = true)
    HideFingerprintIcon -> state.copy(showFingerprintIcon = false)
    ShowBiometricsError -> state.copy(showFingerprintIcon = true, showKeyboard = true)
    ShowKeyboard -> state.copy(showKeyboard = true)
    ShowLoadingCheckingPassword -> state.copy(isLoading = true)
    ShowFailureCheckingPassword -> state.copy(isLoading = false, showPasswordIsIncorrect = true)
    ShowSuccessCheckingPassword -> state.copy(isLoading = false, showKeyboard = false)
    OnEditTextTyping -> state.copy(showPasswordIsIncorrect = false)
    else -> state
  }
  
  override fun onSideEffect(action: StartScreenUserAction) {
    when (action) {
      is OnEnteredPassword -> onEnteredPassword(action.password)
      OnFingerprintIconClicked -> onFingerprintIconClicked()
      else -> Unit
    }
  }
  
  private fun onEnteredPassword(password: String) {
    if (password.isBlank()) return
    applyAction(ShowLoadingCheckingPassword)
    launch {
      val isCorrect = onIoThread { masterPasswordChecker.isCorrect(password) }
      if (isCorrect) {
        MasterPasswordHolder.setMasterPassword(password)
        applyAction(ShowSuccessCheckingPassword)
        router.switchToNewRoot(Screens.ServicesListScreen)
      } else {
        applyAction(ShowFailureCheckingPassword)
      }
    }
  }
  
  private fun onFingerprintIconClicked() {
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
            showSingleEvent(ShowEditTextStubPassword)
            onEnteredPassword(masterPassword)
          }
          is BiometricsPromptEvents.Lockout -> {
            showSingleEvent(ShowTooManyAttemptsTryAgainLater)
          }
          is BiometricsPromptEvents.LockoutPermanent -> {
            showSingleEvent(ShowPermanentLockout)
            applyAction(HideFingerprintIcon)
          }
          is BiometricsPromptEvents.Error -> {
            applyAction(ShowBiometricsError)
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
