package com.arsvechkarev.vault.core.views.drawables

import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

abstract class BaseDrawable : Drawable() {
  
  override fun setAlpha(alpha: Int) = Unit
  
  override fun setColorFilter(colorFilter: ColorFilter?) = Unit
  
  override fun getOpacity(): Int {
    return PixelFormat.OPAQUE
  }
}
