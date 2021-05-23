package com.arsvechkarev.vault.features.password_checking

import buisnesslogic.MasterPasswordChecker
import com.arsvechkarev.vault.core.Dispatchers
import com.arsvechkarev.vault.core.communicators.FlowCommunicator
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingActions.HideDialog
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingActions.ShowDialog
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingReactions.PasswordCheckedSuccessfully
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class PasswordCheckingPresenter @Inject constructor(
  @Named(PasswordCheckingTag)
  private val passwordCheckingCommunicator: FlowCommunicator<PasswordCheckingEvents>,
  private val masterPasswordChecker: MasterPasswordChecker,
  private val dispatchers: Dispatchers,
) : CoroutineScope {
  
  override val coroutineContext = dispatchers.Main + SupervisorJob()
  
  private var view: PasswordCheckingView? = null
  
  init {
    subscribeToPasswordCheckingActions()
  }
  
  fun attachView(view: PasswordCheckingView) {
    this.view = view
  }
  
  fun checkPassword(password: String) {
    view?.showPasswordCheckingLoading()
    launch {
      val isPasswordCorrect = withContext(dispatchers.IO) {
        masterPasswordChecker.isCorrect(password)
      }
      view?.showPasswordCheckingFinished()
      if (isPasswordCorrect) {
        passwordCheckingCommunicator.send(PasswordCheckedSuccessfully)
      } else {
        view?.showPasswordIsIncorrect()
      }
    }
  }
  
  fun detachView() {
    view = null
  }
  
  private fun subscribeToPasswordCheckingActions() {
    launch {
      passwordCheckingCommunicator.events.collect { events ->
        when (events) {
          is ShowDialog -> view?.showDialog()
          is HideDialog -> view?.hideDialog()
        }
      }
    }
  }
}