package com.arsvechkarev.vault.core.views.behaviors

import android.view.View
import android.view.View.MeasureSpec
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import viewdsl.atMost
import viewdsl.hasBehavior

class ScrollingRecyclerBehavior : CoordinatorLayout.Behavior<RecyclerView>() {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: RecyclerView,
        dependency: View
    ): Boolean {
        return dependency.hasBehavior<HeaderBehavior>()
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: RecyclerView,
        dependency: View
    ): Boolean {
        val offset = dependency.bottom - child.top
        ViewCompat.offsetTopAndBottom(child, offset)
        return true
    }

    override fun onMeasureChild(
        parent: CoordinatorLayout,
        child: RecyclerView,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int
    ): Boolean {
        val heightSize = MeasureSpec.getSize(parentHeightMeasureSpec)
        parent.onMeasureChild(
            child,
            parentWidthMeasureSpec,
            widthUsed,
            atMost(heightSize),
            heightUsed
        )
        return true
    }

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: RecyclerView,
        layoutDirection: Int
    ): Boolean {
        val top = findHeader(parent).bottom
        child.layout(0, top, parent.width, top + child.measuredHeight)
        return true
    }

    private fun findHeader(parent: CoordinatorLayout): View {
        repeat(parent.childCount) {
            val child = parent.getChildAt(it)
            if (child.hasBehavior<HeaderBehavior>()) {
                return child
            }
        }
        throw IllegalStateException()
    }
}
