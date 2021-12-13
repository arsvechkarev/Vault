package navigation

import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

fun View.animateVisible(duration: Long) {
  alpha = 0f
  visibility = View.VISIBLE
  animate().alpha(1f).setDuration(duration)
      .setInterpolator(AccelerateDecelerateInterpolator())
      .start()
}

fun View.animateGoneAfter(duration: Long) {
  handler.postDelayed({
    visibility = View.GONE
  }, duration)
}

fun View.animateSlideFromRight(duration: Long) {
  val width = context.resources.displayMetrics.widthPixels
  val height = context.resources.displayMetrics.heightPixels
  alpha = 0f
  translationX = minOf(width, height) / 8f
  visibility = View.VISIBLE
  animate().alpha(1f)
      .translationX(0f)
      .setDuration(duration)
      .withLayer()
      .setInterpolator(AccelerateDecelerateInterpolator())
      .start()
}

fun View.animateSlideToRight(duration: Long) {
  val width = context.resources.displayMetrics.widthPixels
  val height = context.resources.displayMetrics.heightPixels
  val dx = minOf(width, height) / 8f
  animate().alpha(0f)
      .translationX(dx)
      .setDuration(duration)
      .withLayer()
      .setInterpolator(AccelerateDecelerateInterpolator())
      .withEndAction {
        visibility = View.GONE
        translationX = 0f
      }
      .start()
}