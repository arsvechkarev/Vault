package com.arsvechkarev.vault.test.core.views.menu

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.views.menu.AnimatableCircleIconView
import com.arsvechkarev.vault.core.views.menu.MenuContentView
import com.arsvechkarev.vault.core.views.menu.MenuItemView
import com.arsvechkarev.vault.core.views.menu.MenuView
import com.arsvechkarev.vault.test.core.base.baseMatcher
import com.arsvechkarev.vault.test.core.ext.withClassNameTag
import io.github.kakaocup.kakao.common.views.KBaseView
import io.github.kakaocup.kakao.common.views.KView

class KMenuView : KBaseView<KMenuView>({
  withParent {
    withClassNameTag<MenuView>()
    isInstanceOf(MenuView::class.java)
  }
  isInstanceOf(MenuContentView::class.java)
}), KMenuViewAssertions {
  
  private val circleCrossView = KView {
    withParent { isInstanceOf(MenuContentView::class.java) }
    isInstanceOf(AnimatableCircleIconView::class.java)
  }
  
  val importPasswordsMenuItem = KView {
    withMatcher(this@KMenuView.menuItemMatcher(R.drawable.ic_import))
  }
  
  val exportPasswordsMenuItem = KView {
    withMatcher(this@KMenuView.menuItemMatcher(R.drawable.ic_export))
  }
  
  val settingsMenuItem = KView {
    withMatcher(this@KMenuView.menuItemMatcher(R.drawable.ic_settings))
  }
  
  val newPasswordMenuItem = KView {
    withMatcher(this@KMenuView.menuItemMatcher(R.drawable.ic_new_password))
  }
  
  fun open() {
    isClosed()
    circleCrossView.click()
  }
  
  fun close() {
    isOpened()
    circleCrossView.click()
  }
  
  private fun menuItemMatcher(iconRes: Int) = baseMatcher<MenuItemView>(
    descriptionText = "menu item with icon $iconRes",
    matcher = { view -> view.iconRes == iconRes }
  )
}
