package viewdsl

import android.R.attr.state_checked
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
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

fun ImageView.image(@DrawableRes resId: Int, color: Int) {
  val drawable = checkNotNull(context.retrieveDrawable(resId)).apply {
    colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
  }
  setImageDrawable(drawable)
}

fun ImageView.image(drawable: Drawable) {
  setImageDrawable(drawable)
}

fun ImageView.imageTint(color: Int) {
  imageTintList = ColorStateList.valueOf(color)
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

fun SwitchCompat.onCheckedChanged(block: ((Boolean) -> Unit)?) {
  if (block == null) {
    setOnCheckedChangeListener(null)
  } else {
    setOnCheckedChangeListener { _, isChecked -> block(isChecked) }
  }
}

fun SwitchCompat.setCheckedSilently(
  checked: Boolean,
  animate: Boolean,
  onChecked: (Boolean) -> Unit
) {
  onCheckedChanged(null)
  isChecked = checked
  if (!animate) {
    jumpDrawablesToCurrentState()
  }
  onCheckedChanged(onChecked)
}
