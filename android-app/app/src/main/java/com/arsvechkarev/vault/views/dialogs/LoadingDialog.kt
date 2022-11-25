package com.arsvechkarev.vault.views.dialogs

import android.view.ViewGroup
import com.arsvechkarev.vault.viewbuilding.Dimens.ProgressBarSizeBig
import com.arsvechkarev.vault.views.MaterialProgressBar
import com.arsvechkarev.vault.views.SimpleDialog
import navigation.BaseFragmentScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.addView
import viewdsl.size
import viewdsl.tag
import viewdsl.withViewBuilder

const val DialogProgressBar = "DialogProgressBar"

val BaseFragmentScreen.loadingDialog get() = viewAs<SimpleDialog>(DialogProgressBar)

fun ViewGroup.LoadingDialog() = withViewBuilder {
  addView {
    SimpleDialog(context).apply {
      isCancellable = false
      size(MatchParent, MatchParent)
      tag(DialogProgressBar)
      addView {
        MaterialProgressBar(context).apply {
          size(ProgressBarSizeBig, ProgressBarSizeBig)
        }
      }
    }
  }
}
