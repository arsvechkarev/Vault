package com.arsvechkarev.vault.core.views.menu

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.core.graphics.ColorUtils
import com.arsvechkarev.vault.viewbuilding.Colors
import viewdsl.addView
import kotlin.math.abs
import kotlin.math.hypot

class MenuView(context: Context) : FrameLayout(context) {
  
  private var touchedOutsideOfMenuContent = false
  private var latestY = 0f
  private var latestX = 0f
  
  private val menu get() = getChildAt(1) as MenuContentView
  
  init {
    clipChildren = false
    val shadowView = addView {
      View(context).apply {
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
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
  
  override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
    val eventOutsideOfContent = menu.isEventOutsideOfContent(event)
    return menu.opened && event.action == MotionEvent.ACTION_DOWN && eventOutsideOfContent
  }
  
  override fun onTouchEvent(event: MotionEvent): Boolean {
    if (!menu.opened) {
      return false
    }
    when (event.action) {
      MotionEvent.ACTION_DOWN -> {
        touchedOutsideOfMenuContent = menu.isEventOutsideOfContent(event)
        latestX = event.x
        latestY = event.y
        return touchedOutsideOfMenuContent
      }
      
      MotionEvent.ACTION_UP -> {
        val dx = abs(event.x - latestX)
        val dy = abs(event.y - latestY)
        val dst = hypot(dx, dy)
        val scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        if (touchedOutsideOfMenuContent && dst < scaledTouchSlop && menu.opened) {
          menu.onMenuCloseClick()
        }
      }
    }
    return super.onTouchEvent(event)
  }
}
