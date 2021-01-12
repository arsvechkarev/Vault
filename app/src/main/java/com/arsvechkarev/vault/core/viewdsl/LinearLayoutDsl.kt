package com.arsvechkarev.vault.core.viewdsl

import android.widget.LinearLayout

fun LinearLayout.orientation(orientation: Int) {
  this.orientation = orientation
}

fun LinearLayout.gravity(gravity: Int) {
  setGravity(gravity)
}