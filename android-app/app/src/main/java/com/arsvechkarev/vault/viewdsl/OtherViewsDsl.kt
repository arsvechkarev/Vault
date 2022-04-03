package com.arsvechkarev.vault.viewdsl

import android.R.attr.state_checked
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.SeekBar
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

fun ImageView.clearImage() {
    setImageDrawable(null)
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

fun CompoundButton.setCheckedSafe(checked: Boolean) {
    if (isChecked != checked) isChecked = checked
}

fun SwitchCompat.setColors(
    colorThumbEnabled: Int,
    colorThumbDisabled: Int,
    colorTrackEnabled: Int,
    colorTrackDisabled: Int
) {
    thumbTintList = ColorStateList(
        arrayOf(intArrayOf(state_checked), intArrayOf()),
        intArrayOf(
            colorThumbEnabled,
            colorThumbDisabled
        )
    )
    trackTintList = ColorStateList(
        arrayOf(intArrayOf(state_checked), intArrayOf()),
        intArrayOf(
            colorTrackEnabled,
            colorTrackDisabled,
        )
    )
}