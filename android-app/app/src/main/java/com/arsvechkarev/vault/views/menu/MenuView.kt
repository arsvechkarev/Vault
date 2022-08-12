package com.arsvechkarev.vault.views.menu

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.core.graphics.ColorUtils
import com.arsvechkarev.vault.viewbuilding.Colors
import viewdsl.addView

class MenuView(context: Context) : FrameLayout(context) {
  
  private val menu get() = getChildAt(1) as MenuContentView
  
  init {
    clipChildren = false
    val shadowView = addView {
      View(context).apply {
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
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
      }
    }
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
