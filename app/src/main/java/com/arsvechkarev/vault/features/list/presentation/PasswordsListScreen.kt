package com.arsvechkarev.vault.features.list.presentation

import com.arsvechkarev.vault.core.PasswordFilesSaverImpl
import com.arsvechkarev.vault.core.concurrency.AndroidThreader
import com.arsvechkarev.vault.core.extensions.moxyPresenter
import com.arsvechkarev.vault.core.model.PasswordInfo
import com.arsvechkarev.vault.core.navigation.Screen
import com.arsvechkarev.vault.features.list.domain.PasswordsListRepository

class PasswordsListScreen : Screen(), PasswordsListView {
  
  override fun buildLayout() = withViewBuilder {
    RootCoordinatorLayout { }
  }
  
  private val presenter by moxyPresenter {
    PasswordsListPresenter(
      AndroidThreader,
      PasswordsListRepository(PasswordFilesSaverImpl(contextNonNull))
    )
  }
  
  override fun onInit() {
    println("qwerty: loading passwords")
    presenter.loadPasswords()
  }
  
  override fun showNoPasswords() {
    println("qwerty: no passwords")
  }
  
  override fun showPasswordsList(list: List<PasswordInfo>) {
    println("qwerty: passwords = $list")
    
  }
}