package com.arsvechkarev.vault.features.settings

//class SettingsPresenter constructor(
//    private val passwordCheckingCommunicator: Communicator<PasswordCheckingEvents>,
//  private val router: Router,
//  dispatchers: DispatchersFacade,
//) : BaseMviPresenter<SettingsScreenActions, SettingsScreenUserActions, SettingsScreenState>(
//  SettingsScreenUserActions::class,
//  dispatchers
//) {
//
//  init {
//    subscribeToPasswordCheckingEvents()
//  }
//
//  override fun getDefaultState(): SettingsScreenState {
//    return SettingsScreenState()
//  }
//
//  override fun reduce(action: SettingsScreenActions) = when (action) {
//    is ShowFingerprintEnteringEnabled -> {
//      state.copy(fingerprintEnteringEnabled = action.enabled)
//    }
//    is ShowPasswordCheckingDialog -> {
//      state.copy(showPasswordCheckingDialog = true)
//    }
//    is HidePasswordCheckingDialog -> {
//      state.copy(showPasswordCheckingDialog = false)
//    }
//    else -> state
//  }
//
//  override fun onSideEffect(action: SettingsScreenUserActions) {
//    when (action) {
//      OnUserFingerprintTextClicked -> {
//        onUseFingerprintsTextClicked()
//      }
//      is ToggleUseFingerprintForEnteringCheckbox -> {
//        toggleUseFingerprintForEntering(action.enabled)
//      }
//      OnBackPressed -> {
//        if (state.showPasswordCheckingDialog) {
//                    launch { passwordCheckingCommunicator.send(HideDialog) }
//          applyAction(HidePasswordCheckingDialog)
//        } else {
//          router.goBack()
//        }
//      }
//    }
//  }
//
//  private fun onUseFingerprintsTextClicked() {
//    reactToNewTogglingState(false)
//  }
//
//  private fun toggleUseFingerprintForEntering(isChecked: Boolean) {
//    if (false != isChecked) {
//      reactToNewTogglingState(isChecked)
//    }
//  }
//
//  private fun reactToNewTogglingState(isChecked: Boolean) {
//    launch {
//      if (isChecked) {
//        applyAction(ShowPasswordCheckingDialog)
//                passwordCheckingCommunicator.send(ShowDialog)
//         Setting it to false temporarily, so that it doesn't show checked state before user entered password
//        applyAction(ShowFingerprintEnteringEnabled(enabled = false))
//      } else {
//        applyAction(ShowFingerprintEnteringEnabled(enabled = false))
//      }
//    }
//  }
//
//  private fun subscribeToPasswordCheckingEvents() {
//        passwordCheckingCommunicator.events.collectInPresenterScope { event ->
//          when (event) {
//            is PasswordCheckedSuccessfully -> {
//              applyAction(HidePasswordCheckingDialog)
//              passwordCheckingCommunicator.send(HideDialog)
//            }
//          }
//        }
//  }
//}
//
