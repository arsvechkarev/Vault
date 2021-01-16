package com.arsvechkarev.vault

import android.os.Bundle
import com.arsvechkarev.vault.core.BaseActivity
import com.arsvechkarev.vault.core.navigation.NavigatorView
import com.arsvechkarev.vault.core.viewbuilding.Colors
import com.arsvechkarev.vault.core.viewdsl.Densities
import com.arsvechkarev.vault.core.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.core.viewdsl.classNameTag
import com.arsvechkarev.vault.core.viewdsl.size
import com.arsvechkarev.vault.core.viewdsl.withViewBuilder
import com.arsvechkarev.vault.features.start.StartScreen

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
    Densities.init(resources)
    Colors.init(this)
    setContentView(mainActivityLayout)
    navigator.navigate(StartScreen::class)
  }
  
  override fun onBackPressed() {
    if (!navigator.handleGoBack()) {
      super.onBackPressed()
    }
  }
}