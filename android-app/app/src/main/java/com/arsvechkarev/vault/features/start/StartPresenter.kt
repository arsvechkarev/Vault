package com.arsvechkarev.vault.features.start

import android.annotation.SuppressLint
import buisnesslogic.MasterPasswordChecker
import buisnesslogic.MasterPasswordHolder
import com.arsvechkarev.vault.core.DispatchersFacade
import com.arsvechkarev.vault.core.Router
import com.arsvechkarev.vault.core.Screens
import com.arsvechkarev.vault.core.mvi.BaseMviPresenter
import com.arsvechkarev.vault.features.start.StartScreenAction.HideFingerprintIcon
import com.arsvechkarev.vault.features.start.StartScreenAction.ShowBiometricsError
import com.arsvechkarev.vault.features.start.StartScreenAction.ShowFailureCheckingPassword
import com.arsvechkarev.vault.features.start.StartScreenAction.ShowFingerprintIcon
import com.arsvechkarev.vault.features.start.StartScreenAction.ShowKeyboard
import com.arsvechkarev.vault.features.start.StartScreenAction.ShowLoadingCheckingPassword
import com.arsvechkarev.vault.features.start.StartScreenAction.ShowSuccessCheckingPassword
import com.arsvechkarev.vault.features.start.StartScreenUserAction.OnEditTextTyping
import com.arsvechkarev.vault.features.start.StartScreenUserAction.OnEnteredPassword
import com.arsvechkarev.vault.features.start.StartScreenUserAction.OnFingerprintIconClicked
import kotlinx.coroutines.launch

class StartPresenter constructor(
  private val masterPasswordChecker: MasterPasswordChecker,
  private val router: Router,
  dispatchers: DispatchersFacade,
) : BaseMviPresenter<StartScreenAction, StartScreenUserAction, StartScreenState>(
  StartScreenUserAction::class,
  dispatchers
) {
  
  @SuppressLint("NewApi")
  override fun onFirstViewAttach() {
    applyAction(ShowKeyboard)
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
  }
}
