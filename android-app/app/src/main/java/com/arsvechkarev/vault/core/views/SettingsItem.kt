package com.arsvechkarev.vault.core.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles
import com.arsvechkarev.vault.viewbuilding.Styles.AccentTextView
import com.arsvechkarev.vault.viewbuilding.Styles.SecondaryTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.Size.Companion.ZERO
import viewdsl.constraints
import viewdsl.gone
import viewdsl.id
import viewdsl.margin
import viewdsl.margins
import viewdsl.onCheckedChanged
import viewdsl.onClick
import viewdsl.rippleBackground
import viewdsl.setCheckedSilently
import viewdsl.size
import viewdsl.text
import viewdsl.textSize
import viewdsl.textView
import viewdsl.view
import viewdsl.viewAs
import viewdsl.visible
import viewdsl.withViewBuilder

class SettingsItem(context: Context) : ConstraintLayout(context) {
  
  init {
    withViewBuilder {
      isSaveFromParentEnabled = false
      TextView(WrapContent, WrapContent, style = AccentTextView) {
        id(TextTitle)
        textSize(TextSizes.H5)
        margins(start = MarginNormal, top = MarginNormal)
        constraints {
          startToStartOf(parent)
          topToTopOf(parent)
        }
      }
      TextView(ZERO, WrapContent, style = SecondaryTextView) {
        id(TextDescription)
        margins(
          start = MarginNormal,
          top = MarginSmall,
          end = MarginNormal,
          bottom = MarginNormal
        )
      }
      child<SwitchCompat>(WrapContent, WrapContent, style = Styles.Switch) {
        id(Switch)
        gone()
        margin(MarginSmall)
        constraints {
          topToTopOf(parent)
          endToEndOf(parent)
          bottomToBottomOf(parent)
        }
      }
    }
  }
  
  private fun init(
    id: Int,
    @StringRes title: Int,
    @StringRes description: Int,
    clickable: Boolean = false,
    onClick: () -> Unit = {},
    switchEnabled: Boolean = false,
    onSwitchChecked: (Boolean) -> Unit = {}
  ) {
    id(id)
    textView(TextTitle).text(title)
    textView(TextDescription).text(description)
    if (clickable) {
      rippleBackground(Colors.Ripple)
      onClick(onClick)
    }
    if (switchEnabled) {
      viewAs<SwitchCompat>(Switch).apply {
        visible()
        onCheckedChanged(onSwitchChecked)
      }
      view(TextDescription).constraints {
        startToStartOf(parent)
        topToBottomOf(TextTitle)
        endToStartOf(Switch)
        bottomToBottomOf(parent)
      }
    } else {
      view(Switch).gone()
      view(TextDescription).constraints {
        startToStartOf(parent)
        topToBottomOf(TextTitle)
        endToEndOf(parent)
        bottomToBottomOf(parent)
      }
    }
  }
  
  fun setCheckedSilently(checked: Boolean, animate: Boolean, onChecked: (Boolean) -> Unit) {
    viewAs<SwitchCompat>(Switch).setCheckedSilently(checked, animate, onChecked)
  }
  
  companion object {
    
    val TextTitle = View.generateViewId()
    val TextDescription = View.generateViewId()
    val Switch = View.generateViewId()
    
    fun ViewGroup.SettingsItem(
      id: Int,
      @StringRes title: Int,
      @StringRes description: Int,
      clickable: Boolean = false,
      onClick: () -> Unit = {},
      switchEnabled: Boolean = false,
      onSwitchChecked: (Boolean) -> Unit = {}
    ): SettingsItem {
      val settingsItem = SettingsItem(context)
      settingsItem.size(MatchParent, WrapContent)
      addView(settingsItem)
      settingsItem.init(id, title, description, clickable, onClick, switchEnabled, onSwitchChecked)
      return settingsItem
    }
  }
}
