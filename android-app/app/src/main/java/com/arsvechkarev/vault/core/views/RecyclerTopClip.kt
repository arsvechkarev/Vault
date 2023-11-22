package com.arsvechkarev.vault.core.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import androidx.core.graphics.withClip
import androidx.recyclerview.widget.RecyclerView

class RecyclerTopClip(context: Context) : RecyclerView(context) {
  
  private val tempRect = Rect()
  
  var topClip = 0
    set(value) {
      field = value
      invalidate()
    }
  
  override fun dispatchDraw(canvas: Canvas) {
    tempRect.set(0, topClip, width, height)
    canvas.withClip(tempRect) {
      super.dispatchDraw(canvas)
    }
  }
}
