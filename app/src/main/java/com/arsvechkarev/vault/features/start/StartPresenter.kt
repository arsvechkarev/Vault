package com.arsvechkarev.vault.features.start

import android.annotation.SuppressLint
import buisnesslogic.MasterPasswordChecker
import buisnesslogic.MasterPasswordHolder
import com.arsvechkarev.vault.core.BaseCoroutinesPresenter
import com.arsvechkarev.vault.core.Dispatchers
import com.arsvechkarev.vault.core.di.FeatureScope
import com.arsvechkarev.vault.features.common.Screens
import com.arsvechkarev.vault.features.common.biometrics.BiometricsPrompt
import com.arsvechkarev.vault.features.common.biometrics.BiometricsPromptEvents
import com.arsvechkarev.vault.features.common.biometrics.BiometricsStorage
import com.arsvechkarev.vault.features.common.biometrics.SavedFingerprintChecker
import kotlinx.coroutines.flow.collect
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
) : BaseCoroutinesPresenter<StartView>(dispatchers) {
  
  init {
    subscribeToBiometricsEvents()
  }
  
  @SuppressLint("NewApi")
  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    if (fingerprintChecker.isAuthorizationWithUserFingerAvailable()) {
      biometricsPrompt.showDecryptingBiometricsPrompt(biometricsStorage.getIv())
    }
  }
  
  fun onEnteredPassword(password: String) {
    if (password.isBlank()) return
    viewState.showLoadingCheckingPassword()
    coroutine {
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
  
  @SuppressLint("NewApi")
  private fun subscribeToBiometricsEvents() {
    coroutine {
      biometricsPrompt.biometricsEvents().collect { event ->
        when (event) {
          is BiometricsPromptEvents.Success -> {
            val masterPassword = onIoThread { biometricsStorage.getMasterPassword(event.result) }
            require(masterPassword.isNotEmpty())
            onEnteredPassword(masterPassword)
          }
          is BiometricsPromptEvents.Error -> {
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