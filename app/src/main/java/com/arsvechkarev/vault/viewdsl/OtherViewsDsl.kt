package com.arsvechkarev.vault.viewdsl

import android.R.attr.state_checked
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.SeekBar
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.vault.core.extensions.ifNotNull

fun RecyclerView.allowRecyclerScrolling(): Boolean {
  adapter.ifNotNull { adapter ->
    var pos = -1
    for (i in 0 until childCount) {
      val view = getChildAt(i)
      if (view.bottom > height) {
        pos = i
      }
    }
    if (pos == -1) {
      return false
    }
    return pos < adapter.itemCount
  }
  return false
}

fun RecyclerView.setupWith(adapter: RecyclerView.Adapter<*>) {
  layoutManager = LinearLayoutManager(context)
  this.adapter = adapter
}

fun ImageView.image(@DrawableRes resId: Int) {
  setImageResource(resId)
}

fun ImageView.image(drawable: Drawable) {
  setImageDrawable(drawable)
}

inline fun SeekBar.onProgressChanged(crossinline block: (progress: Int) -> Unit) {
  setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
      block(progress)
    }
  
    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }
  
    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }
  
  })
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