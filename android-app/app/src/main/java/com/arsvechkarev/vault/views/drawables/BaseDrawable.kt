package com.arsvechkarev.vault.views.drawables

import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

abstract class BaseDrawable : Drawable() {
  
  override fun setAlpha(alpha: Int) {
  
  }
  
  override fun setColorFilter(colorFilter: ColorFilter?) {
  
  }
  
  override fun getOpacity(): Int {
    return PixelFormat.OPAQUE
  }
}