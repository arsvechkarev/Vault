package com.arsvechkarev.vault.core.views

import android.content.Context
import android.view.MotionEvent
import android.view.ViewGroup
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.viewbuilding.TextSizes
import viewdsl.children
import viewdsl.onClick

class PasswordActionsView(context: Context) : ViewGroup(context) {

    private var maxItemWidth = 0
    private var isPasswordShown = false

    var onTogglePassword: (isPasswordShown: Boolean) -> Unit = {}
    var reactToClicks: () -> Boolean = { true }

    init {
        val buildMenuItem = { iconRes: Int, titleRes: Int ->
            TextWithImageView(context, iconRes, TextSizes.H4, context.getString(titleRes))
        }
        addView(buildMenuItem(R.drawable.ic_copy, R.string.text_copy))
        addView(buildMenuItem(R.drawable.ic_edit, R.string.text_edit))
        addView(buildMenuItem(R.drawable.ic_eye_opened, R.string.text_show))
        val showHideView = getChildAt(2) as TextWithImageView
        showHideView.onClick {
            isPasswordShown = !isPasswordShown
            onTogglePassword(isPasswordShown)
            if (isPasswordShown) {
                showHideView.setImage(R.drawable.ic_eye_closed)
                showHideView.setText(R.string.text_hide)
            } else {
                showHideView.setImage(R.drawable.ic_eye_opened)
                showHideView.setText(R.string.text_show)
            }
        }
    }

    fun showOpenedEye() {
        isPasswordShown = false
        val showHideView = getChildAt(2) as TextWithImageView
        showHideView.setImage(R.drawable.ic_eye_opened)
        showHideView.setText(R.string.text_show)
    }

    fun onCopyClicked(block: () -> Unit) {
        getChildAt(0).onClick(block)
    }

    fun onEditClicked(block: () -> Unit) {
        getChildAt(1).onClick(block)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var maxItemHeight = 0
        children.forEach { child ->
            child.measure(widthMeasureSpec, heightMeasureSpec)
            maxItemHeight = maxOf(maxItemHeight, child.measuredHeight)
            maxItemWidth = maxOf(maxItemWidth, child.measuredWidth)
        }
        setMeasuredDimension(
            resolveSize(maxItemWidth * childCount, widthMeasureSpec),
            resolveSize(maxItemHeight, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childWidth = width / childCount
        var left = 0
        for (child in children) {
            child.layout(left, 0, left + childWidth, child.measuredHeight)
            left = child.right
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return reactToClicks() && super.dispatchTouchEvent(ev)
    }
}
