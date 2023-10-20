package viewdsl

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.shapes.RoundRectShape
import android.graphics.drawable.shapes.Shape
import android.view.View

fun View.backgroundRoundRect(cornerRadius: Int, color: Int) {
  val r = cornerRadius.toFloat()
  val outerRadius = floatArrayOf(r, r, r, r, r, r, r, r)
  backgroundRectWithRadius(outerRadius, color)
}

fun View.backgroundTopRoundRect(cornerRadius: Int, color: Int) {
  val r = cornerRadius.toFloat()
  val outerRadius = floatArrayOf(r, r, r, r, 0f, 0f, 0f, 0f)
  backgroundRectWithRadius(outerRadius, color)
}

private fun View.backgroundRectWithRadius(outerRadius: FloatArray, color: Int) {
  val roundRectShape = RoundRectShape(outerRadius, null, null)
  val backgroundRect = ShapeDrawable().apply {
    shape = roundRectShape
    paint.color = color
  }
  background(backgroundRect)
}

fun View.backgroundCircle(color: Int) {
  val background = ShapeDrawable().apply {
    shape = OvalShape()
    paint.color = color
  }
  background(background)
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

fun View.circleRippleBackground(rippleColor: Int, backgroundColor: Int = Color.TRANSPARENT) {
  addRippleBackground(OvalShape(), backgroundColor, rippleColor)
}

fun View.rippleBackground(rippleColor: Int) {
  addRippleBackground(RectShape(), Color.TRANSPARENT, rippleColor)
}

private fun View.addRippleBackground(shape: Shape, backgroundColor: Int, rippleColor: Int) {
  val background = ShapeDrawable().apply {
    this.shape = shape
    paint.color = backgroundColor
  }
  val mask = ShapeDrawable().apply {
    this.shape = shape
    paint.color = rippleColor
  }
  isClickable = true
  isFocusable = true
  background(RippleDrawable(ColorStateList.valueOf(rippleColor), background, mask))
}
