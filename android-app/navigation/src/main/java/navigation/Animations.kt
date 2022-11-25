package navigation

import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

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
