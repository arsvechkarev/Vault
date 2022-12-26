package com.arsvechkarev.vault.test.core.views.menu

import androidx.test.espresso.assertion.ViewAssertions
import com.arsvechkarev.vault.core.views.menu.MenuContentView
import com.arsvechkarev.vault.core.views.menu.MenuItemView
import com.arsvechkarev.vault.test.core.base.baseMatcher
import io.github.kakaocup.kakao.common.assertions.BaseAssertions

interface KMenuViewAssertions : BaseAssertions {
  
  fun isOpened() {
    view.check(
      ViewAssertions.matches(
        baseMatcher<MenuContentView>(
          descriptionText = "checking that menu is opened",
          matcher = { view -> view.opened }
        )
      )
    )
  }
  
  fun isClosed() {
    view.check(
      ViewAssertions.matches(
        baseMatcher<MenuContentView>(
          descriptionText = "checking that menu is closed",
          matcher = { view -> !view.opened }
        )
      )
    )
  }
  
  fun itemAtPosition(
    position: Int,
    iconRes: Int,
    text: String
  ) {
    view.check(
      ViewAssertions.matches(
        baseMatcher<MenuContentView>(
          descriptionText = "checking that item at position $position " +
              "has text \"$text\" and icon \"$iconRes\"",
          matcher = { view ->
            (view.getChildAt(position + 1) as MenuItemView).run {
              this@run.iconRes == iconRes && this@run.text == text
            }
          }
        )
      )
    )
  }
}
