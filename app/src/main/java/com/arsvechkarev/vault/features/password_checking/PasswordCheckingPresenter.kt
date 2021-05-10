package com.arsvechkarev.vault.features.password_checking

import buisnesslogic.MasterPasswordChecker
import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Dispatchers
import com.arsvechkarev.vault.core.communicators.FlowCommunicator
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingActions.HideDialog
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingActions.ShowDialog
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingReactions.PasswordCheckedSuccessfully
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class PasswordCheckingPresenter @Inject constructor(
  @Named(PasswordCheckingTag)
  private val passwordCheckingCommunicator: FlowCommunicator<PasswordCheckingEvents>,
  private val masterPasswordChecker: MasterPasswordChecker,
  dispatchers: Dispatchers,
) : BasePresenter<PasswordCheckingView>(dispatchers) {
  
  private var view: PasswordCheckingView? = null
  
  init {
    subscribeToPasswordCheckingActions()
  }
  
  override fun attachView(view: PasswordCheckingView) {
    this.view = view
  }
  
  fun checkPassword(password: String) {
    view?.showPasswordCheckingLoading()
    launch {
      val isPasswordCorrect = onIoThread { masterPasswordChecker.isCorrect(password) }
      view?.showPasswordCheckingFinished()
      if (isPasswordCorrect) {
        passwordCheckingCommunicator.send(PasswordCheckedSuccessfully)
      } else {
        view?.showPasswordIsIncorrect()
      }
    }
  }
  
  fun detachView() {
    super.onDestroy()
    view = null
  }
  
  private fun subscribeToPasswordCheckingActions() {
    passwordCheckingCommunicator.events.collectInPresenterScope { events ->
      when (events) {
        is ShowDialog -> view?.showDialog()
        is HideDialog -> view?.hideDialog()
      }
    }
  }
}