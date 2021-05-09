package com.arsvechkarev.vault.features.start

import buisnesslogic.MasterPasswordChecker
import buisnesslogic.MasterPasswordHolder
import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.di.FeatureScope
import com.arsvechkarev.vault.features.common.Screens
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
        onMainThread {
          viewState.showSuccess()
          router.switchToNewRoot(Screens.ServicesListScreen)
        }
      } else {
        onMainThread { viewState.showError() }
      }
    }
  }
}