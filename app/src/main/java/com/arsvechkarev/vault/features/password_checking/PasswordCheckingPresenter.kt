package com.arsvechkarev.vault.features.password_checking

import buisnesslogic.MasterPasswordChecker
import com.arsvechkarev.vault.core.BasePresenterWithChannels
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.communicators.Communicator
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingActions.HideDialog
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingActions.ShowDialog
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingReactions.PasswordCheckedSuccessfully
import javax.inject.Inject
import javax.inject.Named

class PasswordCheckingPresenter @Inject constructor(
  @Named(PasswordCheckingTag)
  private val passwordCheckingCommunicator: Communicator<PasswordCheckingEvents>,
  private val masterPasswordChecker: MasterPasswordChecker,
  threader: Threader,
) : BasePresenterWithChannels<PasswordCheckingView>(threader) {
  
  private var view: PasswordCheckingView? = null
  
  init {
    subscribeToCommunicator(passwordCheckingCommunicator) { events ->
      when (events) {
        is ShowDialog -> {
          view?.showDialog()
        }
        is HideDialog -> {
          view?.hideDialog()
        }
      }
    }
  }
  
  override fun attachView(view: PasswordCheckingView) {
    this.view = view
  }
  
  fun checkPassword(password: String) {
    view?.showPasswordCheckingLoading()
    threader.onIoThread {
      val isPasswordCorrect = masterPasswordChecker.isCorrect(password)
      threader.onMainThread {
        view?.showPasswordCheckingFinished()
        if (isPasswordCorrect) {
          passwordCheckingCommunicator.send(PasswordCheckedSuccessfully)
        } else {
          view?.showPasswordIsIncorrect()
        }
      }
    }
  }
  
  fun detachView() {
    super.onDestroy()
    view = null
  }
}