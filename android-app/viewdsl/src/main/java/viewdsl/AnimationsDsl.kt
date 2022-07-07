@file:Suppress("UsePropertyAccessSyntax")

package viewdsl

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.drawable.Animatable
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import viewdsl.ViewDslConfiguration.Durations

val AccelerateDecelerateInterpolator = AccelerateDecelerateInterpolator()
val OvershootInterpolator = OvershootInterpolator()

fun Animator.startIfNotRunning() {
  if (!isRunning) start()
}

fun Animator.cancelIfRunning() {
  if (isRunning) cancel()
}

fun Animatable.startIfNotRunning() {
  if (!isRunning) start()
}

fun Animatable.stopIfRunning() {
  if (isRunning) stop()
}

fun Animator.doOnEnd(block: () -> Unit) {
  addListener(object : AnimatorListenerAdapter() {
    override fun onAnimationEnd(animation: Animator?) {
      block()
      removeListener(this)
    }
  })
}

fun View.animateVisible(
  andThen: () -> Unit = {},
  duration: Long = Durations.VisibilityChange
) {
  if (visibility == View.VISIBLE) return
  alpha = 0f
  animate().alpha(1f).setDuration(duration)
      .setInterpolator(AccelerateDecelerateInterpolator)
      .withEndAction(andThen)
      .start()
}

fun View.animateInvisible(
  andThen: () -> Unit = {},
  duration: Long = Durations.VisibilityChange
) {
  if (visibility == View.INVISIBLE) return
  animate().alpha(0f).setDuration(duration)
      .setInterpolator(AccelerateDecelerateInterpolator)
      .withEndAction {
        invisible()
        andThen()
      }
      .start()
}

fun View.rotate() {
  if (tag == "isRotating") {
    return
  }
  tag = "isRotating"
  animate().rotation(540f)
      .withLayer()
      .setDuration(Durations.Rotation)
      .setInterpolator(AccelerateDecelerateInterpolator)
      .withEndAction {
        tag("isNotRotating")
        rotation = 0f
      }
      .start()
}
