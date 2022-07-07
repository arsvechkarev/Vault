package com.arsvechkarev.vault.features.settings

import com.arsvechkarev.vault.core.DispatchersFacade
import com.arsvechkarev.vault.core.Router
import com.arsvechkarev.vault.core.communicators.FlowCommunicator
import com.arsvechkarev.vault.core.mvi.BaseMviPresenter
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingActions.HideDialog
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingActions.ShowDialog
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingCommunicator
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingEvents
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingReactions.PasswordCheckedSuccessfully
import com.arsvechkarev.vault.features.settings.SettingsScreenActions.HidePasswordCheckingDialog
import com.arsvechkarev.vault.features.settings.SettingsScreenActions.ShowFingerprintEnteringEnabled
import com.arsvechkarev.vault.features.settings.SettingsScreenActions.ShowPasswordCheckingDialog
import com.arsvechkarev.vault.features.settings.SettingsScreenUserActions.OnBackPressed
import com.arsvechkarev.vault.features.settings.SettingsScreenUserActions.OnUserFingerprintTextClicked
import com.arsvechkarev.vault.features.settings.SettingsScreenUserActions.ToggleUseFingerprintForEnteringCheckbox
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsPresenter @Inject constructor(
  @PasswordCheckingCommunicator
  private val passwordCheckingCommunicator: FlowCommunicator<PasswordCheckingEvents>,
  private val router: Router,
  dispatchers: DispatchersFacade,
) : BaseMviPresenter<SettingsScreenActions, SettingsScreenUserActions, SettingsScreenState>(
  SettingsScreenUserActions::class,
  dispatchers
) {
  
  init {
    subscribeToPasswordCheckingEvents()
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
    reactToNewTogglingState(false)
  }
  
  private fun toggleUseFingerprintForEntering(isChecked: Boolean) {
    if (false != isChecked) {
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
        applyAction(ShowFingerprintEnteringEnabled(enabled = false))
      }
    }
  }
  
  private fun subscribeToPasswordCheckingEvents() {
    passwordCheckingCommunicator.events.collectInPresenterScope { event ->
      when (event) {
        is PasswordCheckedSuccessfully -> {
          applyAction(HidePasswordCheckingDialog)
          passwordCheckingCommunicator.send(HideDialog)
        }
      }
    }
  }
}
