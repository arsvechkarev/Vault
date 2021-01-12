package com.arsvechkarev.vault

import android.os.Bundle
import com.arsvechkarev.vault.core.BaseActivity
import com.arsvechkarev.vault.core.navigation.NavigatorView
import com.arsvechkarev.vault.features.list.presentation.PasswordsListScreen
import com.arsvechkarev.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.viewdsl.classNameTag
import com.arsvechkarev.viewdsl.size
import com.arsvechkarev.viewdsl.withViewBuilder

class MainActivity : BaseActivity() {
  
  private val navigator get() = viewAs<NavigatorView>()
  
  private val mainActivityLayout
    get() = withViewBuilder {
      NavigatorView(context).apply {
        classNameTag()
        size(MatchParent, MatchParent)
      }
    }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(mainActivityLayout)
    navigator.navigate(PasswordsListScreen::class)
  }
}