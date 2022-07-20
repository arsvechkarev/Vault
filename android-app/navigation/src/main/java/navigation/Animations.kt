package navigation

import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

fun View.animateVisible(duration: Long, doOnEnd: () -> Unit) {
  alpha = 0f
  visibility = View.VISIBLE
  animate().alpha(1f).setDuration(duration)
      .setInterpolator(AccelerateDecelerateInterpolator())
      .withEndAction(doOnEnd)
      .start()
}

fun View.animateGoneAfter(duration: Long, doOnEnd: () -> Unit) {
  handler.postDelayed({
    visibility = View.GONE
    doOnEnd()
  }, duration)
}

fun View.animateSlideFromRight(duration: Long, doOnEnd: () -> Unit) {
  val width = context.resources.displayMetrics.widthPixels
  val height = context.resources.displayMetrics.heightPixels
  alpha = 0f
  translationX = minOf(width, height) / 8f
  visibility = View.VISIBLE
  animate().alpha(1f)
      .translationX(0f)
      .setDuration(duration)
      .withLayer()
      .withEndAction(doOnEnd)
      .setInterpolator(AccelerateDecelerateInterpolator())
      .start()
}

fun View.animateSlideToRight(duration: Long, doOnEnd: () -> Unit) {
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
        doOnEnd()
      }
      .start()
}