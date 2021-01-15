package com.arsvechkarev.vault.core.extensions

import android.graphics.Paint

fun Any.Paint(
  color: Int
) = android.graphics.Paint(Paint.ANTI_ALIAS_FLAG).apply {
  this.color = color
}
