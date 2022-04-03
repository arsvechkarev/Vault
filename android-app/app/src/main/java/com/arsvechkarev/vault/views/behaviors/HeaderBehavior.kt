package com.arsvechkarev.vault.views.behaviors

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

/**
 * Behavior for header view in coordinator layout
 */
class HeaderBehavior : CoordinatorLayout.Behavior<View>() {

    private var topAndBottomOffset = 0

    /**
     * Determines whether the header should respond to touch events
     */
    var isScrollable: Boolean = true

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View, layoutDirection: Int
    ): Boolean {
        parent.onLayoutChild(child, layoutDirection)
        ViewCompat.offsetTopAndBottom(child, topAndBottomOffset)
        return true
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return isScrollable
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        scrollHeader(coordinatorLayout, target as RecyclerView, child, dy, consumed)
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        scrollHeader(coordinatorLayout, target as RecyclerView, child, dyUnconsumed, consumed)
    }

    private fun scrollHeader(
        coordinatorLayout: CoordinatorLayout,
        recyclerView: RecyclerView,
        headerView: View,
        dy: Int,
        consumed: IntArray
    ) {
        if (!isScrollable) {
            return
        }
        if (headerView.height + recyclerView.height < coordinatorLayout.height) {
            // Recycler fits the screen, no need for scrolling
            return
        }
        if (dy > 0) {
            // Scrolling down
            val minTopAndBottomOffset = coordinatorLayout.height -
                    (recyclerView.height + headerView.height)
            topAndBottomOffset = (headerView.top - dy).coerceAtLeast(minTopAndBottomOffset)
            consumed[1] = -updateOffset(topAndBottomOffset, headerView)
        } else if (recyclerView.computeVerticalScrollOffset() == 0) {
            // Scrolling up
            topAndBottomOffset = (headerView.top - dy).coerceAtMost(0)
            consumed[1] = -updateOffset(topAndBottomOffset, headerView)
        }
    }

    private fun updateOffset(topAndBottomOffset: Int, headerView: View): Int {
        val offset = topAndBottomOffset - headerView.top
        ViewCompat.offsetTopAndBottom(headerView, offset)
        return offset
    }
}