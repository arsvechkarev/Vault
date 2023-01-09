package com.arsvechkarev.vault.core.views.menu

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.annotation.VisibleForTesting
import androidx.core.graphics.ColorUtils
import com.arsvechkarev.vault.viewbuilding.Colors
import viewdsl.addView

class MenuView(context: Context) : FrameLayout(context) {
  
  @VisibleForTesting
  private val menu get() = getChildAt(1) as MenuContentView
  
  init {
    val shadowView = addView {
      View(context).apply {
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        setOnClickListener { if (enableClicksHandling) menu.onMenuCloseClick() }
        isClickable = false
      }
    }
    addView {
      MenuContentView(context).apply {
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
          gravity = Gravity.END or Gravity.BOTTOM
        }
        onAnimating = { fraction ->
          val color = ColorUtils.blendARGB(Color.TRANSPARENT, Colors.Shadow, fraction)
          shadowView.setBackgroundColor(color)
        }
        onMenuOpened = { shadowView.isClickable = true }
        onMenuClosed = { shadowView.isClickable = false }
      }
    }
  }
  
  var enableClicksHandling = true
  
  fun items(vararg items: MenuItemModel) {
    menu.addItems(*items)
  }
  
  fun onMenuOpenClick(block: () -> Unit) {
    menu.onMenuOpenClick = block
  }
  
  fun onMenuCloseClick(block: () -> Unit) {
    menu.onMenuCloseClick = block
  }
  
  fun openMenu(animate: Boolean = true) {
    menu.openMenu(animate)
  }
  
  fun closeMenu(animate: Boolean = true) {
    menu.closeMenu(animate)
  }
}
