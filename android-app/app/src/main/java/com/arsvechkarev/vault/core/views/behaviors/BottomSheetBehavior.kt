package com.arsvechkarev.vault.core.views.behaviors

import android.animation.ValueAnimator
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_POINTER_DOWN
import android.view.MotionEvent.ACTION_POINTER_UP
import android.view.MotionEvent.ACTION_UP
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.customview.widget.ViewDragHelper.INVALID_POINTER
import com.arsvechkarev.vault.core.views.behaviors.BottomSheetBehavior.State.HIDDEN
import com.arsvechkarev.vault.core.views.behaviors.BottomSheetBehavior.State.SHOWN
import com.arsvechkarev.vault.core.views.behaviors.BottomSheetBehavior.State.SLIDING
import viewdsl.ViewDslConfiguration.applicationContext
import viewdsl.cancelIfRunning
import viewdsl.doOnEnd
import viewdsl.getBehavior
import kotlin.math.abs
import kotlin.math.hypot

class BottomSheetBehavior : CoordinatorLayout.Behavior<View>() {
  
  private val maxFlingVelocity = ViewConfiguration.get(applicationContext)
      .scaledMaximumFlingVelocity
  private val touchSlop = ViewConfiguration.get(applicationContext).scaledTouchSlop
  private var isBeingDragged = false
  private var latestInsideY = -1
  private var latestAnyX = 0f
  private var latestAnyY = 0f
  private var velocityTracker: VelocityTracker? = null
  private var bottomSheetOffsetHelper: BottomSheetOffsetHelper? = null
  private var currentState = HIDDEN
  private var activePointerId = INVALID_POINTER
  private var bottomSheet: View? = null
  private var parentHeight = 0
  private var slideRange = 0
  private val slideAnimator = ValueAnimator().apply {
    addUpdateListener {
      val value = it.animatedValue as Int
      bottomSheetOffsetHelper!!.updateTop(value)
    }
  }
  
  val state get() = currentState
  val isShown get() = state == SHOWN
  
  var onHide: () -> Unit = {}
  var onShow: () -> Unit = {}
  var onSlidePercentageChanged: (Float) -> Unit = {}
  
  fun show() {
    bottomSheet?.post {
      if (currentState == SHOWN) return@post
      currentState = SLIDING
      slideAnimator.cancelIfRunning()
      slideAnimator.duration = DURATION_SLIDE
      slideAnimator.setIntValues(bottomSheet!!.top, parentHeight - slideRange)
      slideAnimator.doOnEnd(onShow)
      slideAnimator.doOnEnd { currentState = SHOWN }
      slideAnimator.start()
    }
  }
  
  fun hide() {
    bottomSheet?.post {
      if (currentState == HIDDEN) return@post
      currentState = SLIDING
      slideAnimator.cancelIfRunning()
      slideAnimator.duration = DURATION_SLIDE
      slideAnimator.setIntValues(bottomSheet!!.top, parentHeight)
      slideAnimator.doOnEnd(onHide)
      slideAnimator.doOnEnd { currentState = HIDDEN }
      slideAnimator.start()
    }
  }
  
  override fun onLayoutChild(parent: CoordinatorLayout, child: View, layoutDirection: Int): Boolean {
    // Should be made clickable because if it isn't, user will be able to click
    // through bottomSheet in opened state, therefore clicking on elements, that
    // he didn't expect to click on
    child.isClickable = true
    child.layout(0, parent.height - child.measuredHeight, parent.width, parent.height)
    bottomSheet = child
    slideRange = child.height
    parentHeight = parent.height
    if (bottomSheetOffsetHelper == null) {
      bottomSheetOffsetHelper = BottomSheetOffsetHelper(
        child,
        onTopChanged = { top ->
          onSlidePercentageChanged((parentHeight - top.toFloat()) / slideRange.toFloat())
        })
    }
    bottomSheetOffsetHelper!!.onViewLayout(parentHeight)
    if (currentState != SHOWN) {
      bottomSheet!!.top = parentHeight
    }
    return true
  }
  
  override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: View, event: MotionEvent): Boolean {
    if (slideAnimator.isRunning || currentState == HIDDEN) return false
    val action = event.action
    if (action == ACTION_MOVE && isBeingDragged) {
      return true
    }
    when (event.actionMasked) {
      ACTION_DOWN -> {
        isBeingDragged = false
        val x = event.x.toInt()
        val y = event.y.toInt()
        latestAnyX = event.x
        latestAnyY = event.y
        if (parent.isPointInChildBounds(child, x, y)) {
          activePointerId = event.getPointerId(0)
          latestInsideY = y
          ensureVelocityTracker()
        }
      }
      ACTION_POINTER_DOWN -> {
        activePointerId = event.getPointerId(event.actionIndex)
        latestInsideY = event.getY(event.actionIndex).toInt()
      }
      ACTION_MOVE -> {
        latestAnyX = event.x
        latestAnyY = event.y
        val pointerIndex = event.findPointerIndex(activePointerId)
        if (pointerIndex == -1) {
          return false
        }
        val y = event.getY(pointerIndex).toInt()
        val yDiff = abs(y - latestInsideY)
        if (yDiff > touchSlop) {
          isBeingDragged = true
          latestInsideY = y
        }
      }
      ACTION_POINTER_UP -> {
        onPointerUp(event)
      }
      ACTION_CANCEL, ACTION_UP -> {
        if (handleUpEvent(parent, child, event)) {
          return true
        }
      }
    }
    velocityTracker?.addMovement(event)
    return isBeingDragged
  }
  
  override fun onTouchEvent(parent: CoordinatorLayout, child: View, event: MotionEvent): Boolean {
    if (slideAnimator.isRunning || currentState == HIDDEN) return false
    when (event.actionMasked) {
      ACTION_DOWN -> {
        val x = event.x.toInt()
        val y = event.y.toInt()
        latestAnyX = event.x
        latestAnyY = event.y
        if (parent.isPointInChildBounds(child, x, y)) {
          latestInsideY = y
          activePointerId = event.getPointerId(0)
          ensureVelocityTracker()
        }
      }
      ACTION_POINTER_DOWN -> {
        activePointerId = event.getPointerId(event.actionIndex)
        latestInsideY = event.getY(event.actionIndex).toInt()
      }
      ACTION_MOVE -> {
        latestAnyX = event.x
        latestAnyY = event.y
        val pointerIndex = event.findPointerIndex(activePointerId)
        if (pointerIndex == -1) {
          return true
        }
        val y = event.getY(pointerIndex).toInt()
        var dy = y - latestInsideY
        if (!isBeingDragged && abs(dy) > touchSlop) {
          isBeingDragged = true
          if (dy > 0) {
            dy -= touchSlop
          } else {
            dy += touchSlop
          }
        }
        if (isBeingDragged) {
          latestInsideY = y
          // We're being dragged so scroll the view
          updateDyOffset(dy)
        }
      }
      ACTION_POINTER_UP -> {
        onPointerUp(event)
      }
      ACTION_UP, ACTION_CANCEL -> {
        handleUpEvent(parent, child, event)
      }
    }
    velocityTracker?.addMovement(event)
    return true
  }
  
  private fun onPointerUp(event: MotionEvent) {
    val actionIndex = event.actionIndex
    if (event.getPointerId(actionIndex) == activePointerId) {
      val newIndex = if (actionIndex == 0) 1 else 0
      activePointerId = event.getPointerId(newIndex)
      latestInsideY = event.getY(newIndex).toInt()
    }
  }
  
  private fun handleUpEvent(parent: CoordinatorLayout, child: View, event: MotionEvent): Boolean {
    if (handleOutsideEvent(parent, child, event)) {
      return true
    }
    velocityTracker?.addMovement(event)
    velocityTracker?.computeCurrentVelocity(1000)
    val yVelocity = velocityTracker?.getYVelocity(activePointerId) ?: 0f
    if (yVelocity / maxFlingVelocity > FLING_VELOCITY_THRESHOLD) {
      currentState = HIDDEN
      val timeInSeconds = abs((parentHeight - bottomSheet!!.top) / yVelocity)
      slideAnimator.duration = (timeInSeconds * 1000).toLong()
      slideAnimator.setIntValues(bottomSheet!!.top, parentHeight)
      slideAnimator.doOnEnd(onHide)
      slideAnimator.start()
    } else if (isBeingDragged) {
      val middlePoint = parentHeight - slideRange * 0.65
      val endY = if (bottomSheet!!.top < middlePoint) {
        currentState = SHOWN
        slideAnimator.doOnEnd(onShow)
        parentHeight - slideRange
      } else {
        currentState = HIDDEN
        slideAnimator.doOnEnd(onHide)
        parentHeight
      }
      slideAnimator.setIntValues(bottomSheet!!.top, endY)
      slideAnimator.duration = DURATION_SLIDE
      slideAnimator.start()
      return true
    }
    endTouch()
    return false
  }
  
  private fun handleOutsideEvent(
    parent: CoordinatorLayout,
    child: View,
    event: MotionEvent
  ): Boolean {
    val dx = abs(event.x - latestAnyX)
    val dy = abs(event.y - latestAnyY)
    val scaledTouchSlop = ViewConfiguration.get(applicationContext).scaledTouchSlop
    val outsideOfBottomSheet = !parent.isPointInChildBounds(child, event.x.toInt(), event.y.toInt())
    if (hypot(dx, dy) < scaledTouchSlop && outsideOfBottomSheet) {
      hide()
      return true
    }
    return false
  }
  
  private fun updateDyOffset(dy: Int) {
    bottomSheetOffsetHelper!!.updateDyOffset(dy)
  }
  
  private fun ensureVelocityTracker() {
    if (velocityTracker == null) {
      velocityTracker = VelocityTracker.obtain()
    }
  }
  
  private fun endTouch() {
    isBeingDragged = false
    activePointerId = INVALID_POINTER
    velocityTracker?.recycle()
    velocityTracker = null
  }
  
  enum class State {
    SHOWN, SLIDING, HIDDEN
  }
  
  companion object {
  
    private const val DURATION_SLIDE = 225L
    private const val FLING_VELOCITY_THRESHOLD = 0.18f
  
    val View.asBottomSheet get() = getBehavior<BottomSheetBehavior>()
  }
}
