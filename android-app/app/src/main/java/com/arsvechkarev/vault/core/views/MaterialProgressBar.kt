package com.arsvechkarev.vault.core.views

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.AnimatedVectorDrawable
import android.view.View
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.viewbuilding.Colors
import viewdsl.retrieveDrawable
import viewdsl.startIfNotRunning
import viewdsl.stopIfRunning

class MaterialProgressBar(context: Context) : View(context) {
  
  private val drawable get() = background as AnimatedVectorDrawable
  
  init {
    setup(Colors.Accent, Thickness.NORMAL)
  }
  
  fun setup(color: Int, thickness: Thickness) {
    background = when (thickness) {
      Thickness.NORMAL -> context.retrieveDrawable(R.drawable.progress_anim_normal)
      Thickness.THICK -> context.retrieveDrawable(R.drawable.progress_anim_thick)
    }.apply {
      colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    drawable.setBounds(0, 0, w, h)
  }
  
  override fun onVisibilityChanged(changedView: View, visibility: Int) {
    if (visibility == VISIBLE) {
      drawable.startIfNotRunning()
    } else {
      drawable.stopIfRunning()
    }
  }
  
  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    drawable.startIfNotRunning()
  }
  
  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    drawable.stopIfRunning()
  }
  
  enum class Thickness {
    NORMAL, THICK
  }
}
