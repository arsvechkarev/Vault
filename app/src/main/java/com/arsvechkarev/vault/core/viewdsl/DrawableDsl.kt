package com.arsvechkarev.vault.core.viewdsl

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.shapes.RoundRectShape
import android.graphics.drawable.shapes.Shape
import android.view.View
import com.arsvechkarev.viewdsl.background

fun Drawable.applyColor(color: Int) {
  colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
}

fun View.backgroundRoundRect(cornerRadius: Int, color: Int) {
  val r = cornerRadius.toFloat()
  val outerRadii = floatArrayOf(r, r, r, r, r, r, r, r)
  val roundRectShape = RoundRectShape(outerRadii, null, null)
  val backgroundRect = ShapeDrawable().apply {
    shape = roundRectShape
    paint.color = color
  }
  background(backgroundRect)
}

fun View.gradientRippleBackground(
  cornerRadius: Int,
  rippleColor: Int,
  gradientStart: Int,
  gradientEnd: Int,
  orientation: GradientDrawable.Orientation
) {
  val bgDrawable = GradientDrawable(orientation, intArrayOf(gradientStart, gradientEnd))
  val r = cornerRadius.toFloat()
  val outerRadii = floatArrayOf(r, r, r, r, r, r, r, r)
  bgDrawable.cornerRadii = outerRadii
  rippleBackground(rippleColor, cornerRadius, bgDrawable)
}

fun View.rippleBackground(
  rippleColor: Int,
  backgroundColor: Int,
  cornerRadius: Int
) {
  val r = cornerRadius.toFloat()
  val outerRadii = floatArrayOf(r, r, r, r, r, r, r, r)
  val roundRectShape = RoundRectShape(outerRadii, null, null)
  val backgroundRect = ShapeDrawable().apply {
    shape = roundRectShape
    paint.color = backgroundColor
  }
  rippleBackground(rippleColor, cornerRadius, backgroundRect)
}

fun View.rippleBackground(
  rippleColor: Int,
  cornerRadius: Int,
  backgroundDrawable: Drawable
) {
  val r = cornerRadius.toFloat()
  val outerRadii = floatArrayOf(r, r, r, r, r, r, r, r)
  val roundRectShape = RoundRectShape(outerRadii, null, null)
  val maskRect = ShapeDrawable().apply {
    shape = roundRectShape
    paint.color = rippleColor
  }
  isClickable = true
  isFocusable = true
  background(RippleDrawable(ColorStateList.valueOf(rippleColor), backgroundDrawable, maskRect))
}

fun View.circleRippleBackground(rippleColor: Int) {
  addRippleBackground(OvalShape(), rippleColor)
}

fun View.rippleBackground(rippleColor: Int) {
  addRippleBackground(RectShape(), rippleColor)
}

private fun View.addRippleBackground(shape: Shape, rippleColor: Int) {
  val background = ShapeDrawable().apply {
    this.shape = shape
    paint.color = Color.TRANSPARENT
  }
  val mask = ShapeDrawable().apply {
    this.shape = shape
    paint.color = rippleColor
  }
  isClickable = true
  isFocusable = true
  background(RippleDrawable(ColorStateList.valueOf(rippleColor), background, mask))
}