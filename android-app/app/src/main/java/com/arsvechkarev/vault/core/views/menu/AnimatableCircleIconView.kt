package com.arsvechkarev.vault.core.views.menu

import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.arsvechkarev.vault.core.extensions.Paint
import viewdsl.layoutGravity
import viewdsl.retrieveDrawable
import viewdsl.size

class AnimatableCircleIconView(
  context: Context,
  size: Int,
  circleColor: Int,
  private val drawableRes1: Int,
  private val drawableRes2: Int,
  private val iconColor: Int,
) : FrameLayout(context) {
  
  private val drawable1: Drawable
    get() = context.retrieveDrawable(drawableRes1).mutate()
        .apply { colorFilter = PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP) }
  
  private val drawable2: Drawable
    get() = context.retrieveDrawable(drawableRes2).mutate()
        .apply { colorFilter = PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP) }
  
  private val animatableView get() = getChildAt(0)
  private val circlePaint = Paint(circleColor)
  
  init {
    addView(View(context).apply {
      size(size, size)
    })
    animatableView.layoutGravity(Gravity.CENTER)
    animatableView.background = drawable1
  }
  
  fun switchToFirstDrawable(animate: Boolean = true) {
    if (animate) {
      animatableView.background = drawable1
      (animatableView.background as AnimatedVectorDrawable).start()
    } else {
      animatableView.background = drawable2
    }
  }
  
  fun switchToSecondDrawable(animate: Boolean = true) {
    if (animate) {
      animatableView.background = drawable2
      (animatableView.background as AnimatedVectorDrawable).start()
    } else {
      animatableView.background = drawable1
    }
  }
  
  override fun dispatchDraw(canvas: Canvas) {
    canvas.drawCircle(width / 2f, height / 2f, width / 2f, circlePaint)
    super.dispatchDraw(canvas)
  }
}
