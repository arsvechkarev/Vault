package com.arsvechkarev.vault.features.start

import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Screens
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.di.FeatureScope
import com.arsvechkarev.vault.cryptography.MasterPasswordChecker
import com.arsvechkarev.vault.cryptography.MasterPasswordHolder
import navigation.Router
import javax.inject.Inject

@FeatureScope
class StartPresenter @Inject constructor(
  private val masterPasswordChecker: MasterPasswordChecker,
  private val router: Router,
  threader: Threader,
) : BasePresenter<StartView>(threader) {
  
  fun onEnteredPassword(password: String) {
    if (password.isBlank()) return
    viewState.showLoading()
    onBackgroundThread {
      val isCorrect = masterPasswordChecker.isCorrect(password)
      if (isCorrect) {
        MasterPasswordHolder.setMasterPassword(password)
        updateViewState { showSuccess() }
        onMainThread { router.switchToNewRoot(Screens.ServicesListScreen) }
      } else {
        updateViewState { showError() }
      }
    }
  }
}