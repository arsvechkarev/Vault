package com.arsvechkarev.vault.core

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.core.graphics.ColorUtils
import com.arsvechkarev.vault.viewbuilding.Colors

object StatusBar {
  
  fun setLightStatusBar(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      val decorView = activity.window.decorView
      var flags: Int = decorView.systemUiVisibility
      flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
      decorView.systemUiVisibility = flags
      activity.window.statusBarColor = Color.WHITE
    }
  }
  
  fun clearLightStatusBar(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      val decorView = activity.window.decorView
      var flags: Int = decorView.systemUiVisibility
      flags = flags and (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv())
      decorView.systemUiVisibility = flags
    }
  }
  
  fun setStatusBarColor(activity: Activity, color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      activity.window.statusBarColor = color
    }
  }
  
  fun applyShadow(
    activity: Activity,
    percentage: Float,
    shadowColor: Int = Colors.Shadow,
    backgroundColor: Int = Colors.Background
  ) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      val endColor = ColorUtils.compositeColors(shadowColor, backgroundColor)
      val statusColor = ColorUtils.blendARGB(backgroundColor, endColor, percentage)
      setStatusBarColor(activity, statusColor)
    }
  }
}