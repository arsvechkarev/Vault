package com.arsvechkarev.vault.features.password_checking

import buisnesslogic.MasterPasswordChecker
import com.arsvechkarev.vault.core.DispatchersFacade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PasswordCheckingPresenter constructor(
  @PasswordCheckingCommunicator
  //  private val passwordCheckingCommunicator: Communicator<PasswordCheckingEvents>,
  private val masterPasswordChecker: MasterPasswordChecker,
  private val dispatchers: DispatchersFacade,
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
        //        passwordCheckingCommunicator.send(PasswordCheckedSuccessfully)
      } else {
        view?.showPasswordIsIncorrect()
      }
    }
  }
  
  fun detachView() {
    view = null
  }
  
  private fun subscribeToPasswordCheckingActions() {
    //    launch {
    //      passwordCheckingCommunicator.events.collect { events ->
    //        when (events) {
    //          is ShowDialog -> view?.showDialog()
    //          is HideDialog -> view?.hideDialog()
    //        }
    //      }
    //    }
  }
}