@file:Suppress("UsePropertyAccessSyntax")

package com.arsvechkarev.vault.viewdsl

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.graphics.drawable.Animatable
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

const val DURATION_VIBRATE_SHORT = 10L
const val DURATION_SHORT = 170L
const val DURATION_DEFAULT = 300L
const val DURATION_MEDIUM = 500L
const val DURATION_LONG = 800L

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

fun View.animateVisible(andThen: () -> Unit = {}, duration: Long = DURATION_DEFAULT) {
  if (alpha == 1f && visibility == View.VISIBLE) {
    andThen()
    return
  }
  alpha = 0f
  visible()
  animate().alpha(1f).setDuration(duration)
      .setInterpolator(AccelerateDecelerateInterpolator)
      .withEndAction(andThen)
      .start()
}

fun View.animateInvisible(andThen: () -> Unit = {}, duration: Long = DURATION_DEFAULT) {
  animate().alpha(0f).setDuration(duration)
      .setInterpolator(AccelerateDecelerateInterpolator)
      .withEndAction {
        invisible()
        andThen()
      }
      .start()
}

fun View.animateGone(andThen: () -> Unit = {}, duration: Long = DURATION_DEFAULT) {
  if (visibility == GONE) {
    andThen()
    return
  }
  animate().alpha(0f).setDuration(duration)
      .setInterpolator(AccelerateDecelerateInterpolator)
      .withEndAction {
        gone()
        andThen()
      }
      .start()
}

fun View.animateSlideFromRight(duration: Long = DURATION_SHORT) {
  val width = context.resources.displayMetrics.widthPixels
  val height = context.resources.displayMetrics.heightPixels
  alpha = 0f
  translationX = minOf(width, height) / 8f
  visible()
  animate().alpha(1f)
      .translationX(0f)
      .setDuration(duration)
      .withLayer()
      .setInterpolator(AccelerateDecelerateInterpolator)
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
      .setInterpolator(AccelerateDecelerateInterpolator)
      .withEndAction {
        gone()
        translationX = 0f
      }
      .start()
}

fun animateVisible(vararg views: View, andThen: () -> Unit = {}) {
  var andThenPosted = false
  for (view in views) {
    if (!andThenPosted) {
      view.animateVisible(andThen)
      andThenPosted = true
    } else {
      view.animateVisible()
    }
  }
}

fun animateInvisible(vararg views: View, andThen: () -> Unit = {}) {
  var andThenPosted = false
  for (view in views) {
    if (!andThenPosted) {
      view.animateInvisible(andThen)
      andThenPosted = true
    } else {
      view.animateInvisible()
    }
  }
}

fun View.animateColor(startColor: Int, endColor: Int, andThen: () -> Unit = {}) {
  ObjectAnimator.ofObject(this,
    "backgroundColor", ArgbEvaluator(), startColor, endColor).apply {
    duration = DURATION_DEFAULT
    interpolator = FastOutSlowInInterpolator()
    if (andThen != {}) {
      doOnEnd(andThen)
    }
    start()
  }
}

fun ViewGroup.animateChildrenVisible() = forEachChild { it.animateVisible() }

fun ViewGroup.animateChildrenInvisible() = forEachChild { it.animateInvisible() }
