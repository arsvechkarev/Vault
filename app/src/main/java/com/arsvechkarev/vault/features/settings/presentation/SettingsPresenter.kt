package com.arsvechkarev.vault.features.settings.presentation

import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.di.FeatureScope
import navigation.Router
import javax.inject.Inject

@FeatureScope
class SettingsPresenter @Inject constructor(
  private val router: Router,
  threader: Threader
) : BasePresenter<SettingsView>(threader) {
  
  private var isChecked = false
  
  override fun onFirstViewAttach() {
    viewState.showUseFingerprintForEnteringEnabled(isChecked)
  }
  
  fun onUseFingerprintsTextClicked() {
    isChecked = !isChecked
    viewState.showUseFingerprintForEnteringEnabled(isChecked)
  }
  
  fun toggleUseFingerprintForEntering(isChecked: Boolean) {
    if (this.isChecked != isChecked) {
      this.isChecked = isChecked
      viewState.showUseFingerprintForEnteringEnabled(this.isChecked)
    }
  }
  
  fun onBackClicked() {
    router.goBack()
  }
}