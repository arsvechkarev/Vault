package com.arsvechkarev.vault.viewdsl

import android.R.attr.state_checked
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.SwitchCompat

fun ImageView.image(@DrawableRes resId: Int) {
  setImageResource(resId)
}

fun ImageView.image(drawable: Drawable) {
  setImageDrawable(drawable)
}

fun SwitchCompat.setColors(
  colorThumbEnabled: Int,
  colorThumbDisabled: Int,
  colorTrackEnabled: Int,
  colorTrackDisabled: Int
) {
  thumbTintList = ColorStateList(arrayOf(intArrayOf(state_checked), intArrayOf()),
    intArrayOf(
      colorThumbEnabled,
      colorThumbDisabled
    ))
  trackTintList = ColorStateList(arrayOf(intArrayOf(state_checked), intArrayOf()),
    intArrayOf(
      colorTrackEnabled,
      colorTrackDisabled,
    ))
}