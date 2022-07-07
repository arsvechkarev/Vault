package com.arsvechkarev.vault.views.behaviors

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import viewdsl.hasBehavior

class ViewUnderHeaderBehavior : CoordinatorLayout.Behavior<View>() {
  
  override fun layoutDependsOn(
    parent: CoordinatorLayout,
    child: View,
    dependency: View
  ): Boolean {
    return dependency.hasBehavior<HeaderBehavior>()
  }
  
  override fun onDependentViewChanged(
    parent: CoordinatorLayout,
    child: View,
    dependency: View
  ): Boolean {
    child.top = dependency.bottom
    return true
  }
  
  override fun onMeasureChild(
    parent: CoordinatorLayout,
    child: View,
    parentWidthMeasureSpec: Int,
    widthUsed: Int,
    parentHeightMeasureSpec: Int,
    heightUsed: Int
  ): Boolean {
    val header = findHeader(parent)
    val height = parent.measuredHeight - header.measuredHeight
    val heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
    parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, heightSpec, heightUsed)
    return true
  }
  
  override fun onLayoutChild(
    parent: CoordinatorLayout,
    child: View,
    layoutDirection: Int
  ): Boolean {
    val top = findHeader(parent).bottom
    child.layout(0, top, parent.width, parent.height)
    return true
  }
  
  private fun findHeader(parent: CoordinatorLayout): View {
    repeat(parent.childCount) {
      val child = parent.getChildAt(it)
      if (child.hasBehavior<HeaderBehavior>()) {
        return child
      }
    }
    throw IllegalStateException()
  }
}