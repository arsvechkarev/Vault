package com.arsvechkarev.vault.views

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.AnimatedVectorDrawable
import android.view.View
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.viewbuilding.Colors
import viewdsl.retrieveDrawable
import viewdsl.stopIfRunning
import viewdsl.visible

class AnimatableCheckmark(context: Context) : View(context) {

    private val drawable get() = background as AnimatedVectorDrawable

    init {
        background = context.retrieveDrawable(R.drawable.avd_checkmark).apply { }
        background.colorFilter = PorterDuffColorFilter(Colors.Correct, PorterDuff.Mode.SRC_ATOP)
    }

    fun animateCheckmark(andThen: () -> Unit = {}) {
        visible()
        drawable.start()
        invalidate()
        handler.postDelayed(andThen, CHECKMARK_DELAY)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        drawable.stopIfRunning()
    }

    companion object {

        const val CHECKMARK_DELAY = 1300L
    }
}