package com.arsvechkarev.vault.features.list.presentation

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.AndroidThreader
import com.arsvechkarev.vault.core.extensions.moxyPresenter
import com.arsvechkarev.vault.core.model.PasswordInfo
import com.arsvechkarev.vault.core.navigation.Screen
import com.arsvechkarev.vault.features.list.domain.PasswordsListRepository
import com.arsvechkarev.vault.password.PasswordsSaverImpl
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textSize

class PasswordsListScreen : Screen(), PasswordsListView {
  
  override fun buildLayout() = withViewBuilder {
    RootCoordinatorLayout {
      TextView(MatchParent, WrapContent, style = BoldTextView) {
        textSize(TextSizes.H0)
        text(getString(R.string.text_passwords))
      }
    }
  }
  
  private val presenter by moxyPresenter {
    PasswordsListPresenter(
      AndroidThreader,
      PasswordsListRepository(PasswordsSaverImpl(contextNonNull))
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