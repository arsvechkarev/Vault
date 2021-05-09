package com.arsvechkarev.vault.features.settings.presentation

import com.arsvechkarev.vault.core.BasePresenterWithChannels
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.communicators.Communicator
import com.arsvechkarev.vault.core.di.FeatureScope
import com.arsvechkarev.vault.features.common.fingerprints.SavedFingerprintChecker
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingActions
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingEvents
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingReactions.PasswordCheckedSuccessfully
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingTag
import navigation.Router
import javax.inject.Inject
import javax.inject.Named

@FeatureScope
class SettingsPresenter @Inject constructor(
  @Named(PasswordCheckingTag)
  private val passwordCheckingCommunicator: Communicator<PasswordCheckingEvents>,
  private val fingerprintChecker: SavedFingerprintChecker,
  private val router: Router,
  threader: Threader
) : BasePresenterWithChannels<SettingsView>(threader) {
  
  init {
    subscribeToCommunicator(passwordCheckingCommunicator) { events ->
      when (events) {
        is PasswordCheckedSuccessfully -> {
          passwordCheckingCommunicator.send(PasswordCheckingActions.HideDialog)
          fingerprintChecker.setAuthorizationWithUserFingerAvailable(true)
          viewState.showUseFingerprintForEnteringEnabled(true)
        }
      }
    }
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
  
  private fun reactToNewTogglingState(isChecked: Boolean) {
    if (isChecked) {
      passwordCheckingCommunicator.send(PasswordCheckingActions.ShowDialog)
      // Setting it to false temporarily, so that it doesn't show checked state before user entered password
      viewState.showUseFingerprintForEnteringEnabled(false)
    } else {
      fingerprintChecker.setAuthorizationWithUserFingerAvailable(false)
      viewState.showUseFingerprintForEnteringEnabled(false)
    }
  }
  
  fun onBackClicked() {
    router.goBack()
  }
}