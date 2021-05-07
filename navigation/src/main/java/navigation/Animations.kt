package navigation

import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

const val DURATION_SHORT = 500L

fun View.animateVisible(duration: Long = DURATION_SHORT) {
  alpha = 0f
  visibility = View.VISIBLE
  animate().alpha(1f).setDuration(duration)
      .setInterpolator(AccelerateDecelerateInterpolator())
      .start()
}

fun View.animateGoneAfter(duration: Long = DURATION_SHORT) {
  handler.postDelayed({
    visibility = View.GONE
  }, duration)
}

fun View.animateSlideFromRight(duration: Long = DURATION_SHORT) {
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

fun View.animateSlideToRight(duration: Long = DURATION_SHORT) {
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