package com.arsvechkarev.vault.test.core.interceptors

import androidx.test.espresso.ViewInteraction
import com.arsvechkarev.vault.core.views.menu.AnimatableCircleIconView
import com.arsvechkarev.vault.core.views.menu.MenuContentView
import com.arsvechkarev.vault.test.core.ext.isAllowed
import com.kaspersky.kaspresso.interceptors.behavior.ViewBehaviorInterceptor
import io.github.kakaocup.kakao.common.views.KView

class OpenMenuIfClosedInterceptor(
  private val allowedExceptions: Set<Class<out Throwable>>
) : ViewBehaviorInterceptor {
  
  private val circleCrossView = KView {
    withParent { isInstanceOf(MenuContentView::class.java) }
    isInstanceOf(AnimatableCircleIconView::class.java)
  }
  
  override fun <T> intercept(interaction: ViewInteraction, action: () -> T): T {
    return try {
      action.invoke()
    } catch (error: Throwable) {
      if (error.isAllowed(allowedExceptions)) {
        return openMenu(action, error)
      }
      throw error
    }
  }
  
  private fun <T> openMenu(action: () -> T, cachedError: Throwable): T {
    return try {
      circleCrossView.click()
      action.invoke()
    } catch (error: Throwable) {
      throw cachedError
    }
  }
}
