package com.arsvechkarev.vault.core.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.views.Snackbar.Type.CHECKMARK
import com.arsvechkarev.vault.core.views.Snackbar.Type.ERROR
import com.arsvechkarev.vault.features.common.Durations
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens
import com.arsvechkarev.vault.viewbuilding.Dimens.CheckmarkSize
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import navigation.BaseFragmentScreen
import viewdsl.AccelerateDecelerateInterpolator
import viewdsl.Size.Companion.WrapContent
import viewdsl.Size.IntSize
import viewdsl.addView
import viewdsl.backgroundRoundRect
import viewdsl.classNameTag
import viewdsl.constraints
import viewdsl.dp
import viewdsl.gone
import viewdsl.id
import viewdsl.image
import viewdsl.imageTint
import viewdsl.invisible
import viewdsl.padding
import viewdsl.paddings
import viewdsl.size
import viewdsl.text
import viewdsl.textSize
import viewdsl.textView
import viewdsl.view
import viewdsl.viewAs
import viewdsl.visible
import viewdsl.withViewBuilder

class Snackbar(context: Context) : ConstraintLayout(context) {
  
  private var opened = false
  
  private val dist = 70.dp.toFloat()
  
  init {
    withViewBuilder {
      classNameTag()
      size(WrapContent, WrapContent)
      clipToPadding = false
      invisible()
      paddings(start = MarginNormal, end = MarginNormal, top = MarginSmall, bottom = MarginSmall)
      ImageView(WrapContent, WrapContent) {
        id(ImageError)
        image(R.drawable.ic_error)
        imageTint(Colors.TextPrimary)
      }
      addView {
        AnimatableCheckmark(context).apply {
          id(Checkmark)
          size(IntSize((CheckmarkSize * 1.3f).toInt()), IntSize(CheckmarkSize))
          padding(MarginSmall)
        }
      }
      TextView(WrapContent, WrapContent, style = BaseTextView) {
        id(Text)
        textSize(TextSizes.H6)
      }
    }
  }
  
  fun show(type: Type, textResId: Int) {
    if (opened) return
    opened = true
    translationY = dist
    visible()
    val color = if (type == CHECKMARK) Colors.SnackbarDefault else Colors.SnackbarError
    backgroundRoundRect(Dimens.CornerRadiusDefault, color)
    textView(Text).text(textResId)
    when (type) {
      CHECKMARK -> {
        view(ImageError).gone()
        view(Checkmark).constraints {
          startToStartOf(parent)
          topToTopOf(parent)
          bottomToBottomOf(parent)
        }
        view(Text).paddings(start = (CheckmarkSize * 1.3f).toInt() + MarginNormal)
        view(Text).constraints {
          startToStartOf(parent)
          topToTopOf(parent)
          endToEndOf(parent)
          bottomToBottomOf(parent)
        }
        viewAs<AnimatableCheckmark>(Checkmark).animateCheckmark()
      }
      ERROR -> {
        view(Checkmark).gone()
        view(ImageError).visible()
        view(ImageError).constraints {
          startToStartOf(parent)
          topToTopOf(parent)
          bottomToBottomOf(parent)
        }
        view(Text).paddings(start = ImageSize + MarginNormal)
        view(Text).constraints {
          startToStartOf(parent)
          topToTopOf(parent)
          endToEndOf(parent)
          bottomToBottomOf(parent)
        }
      }
    }
    animate().translationY(0f)
        .setInterpolator(AccelerateDecelerateInterpolator)
        .setDuration(Durations.Short)
        .withLayer()
        .start()
    val delay = Durations.Short + Durations.Checkmark + Durations.Snackbar
    postDelayed({ hide() }, delay)
  }
  
  private fun hide() {
    if (!opened) return
    opened = false
    animate().translationY(dist)
        .setInterpolator(AccelerateDecelerateInterpolator)
        .setDuration(Durations.Short)
        .withLayer()
        .withEndAction { invisible() }
        .start()
  }
  
  enum class Type {
    CHECKMARK, ERROR
  }
  
  companion object {
    
    val BaseFragmentScreen.snackbar get() = viewAs<Snackbar>()
    
    private val Text = View.generateViewId()
    private val Checkmark = View.generateViewId()
    private val ImageError = View.generateViewId()
    
    fun ViewGroup.Snackbar(block: Snackbar.() -> Unit) = withViewBuilder {
      val snackbar = Snackbar(context)
      addView(snackbar)
      snackbar.apply(block)
    }
  }
}
