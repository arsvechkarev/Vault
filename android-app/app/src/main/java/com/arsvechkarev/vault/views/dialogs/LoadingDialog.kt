package com.arsvechkarev.vault.views.dialogs

import android.view.ViewGroup
import com.arsvechkarev.vault.viewbuilding.Dimens.ProgressBarSizeBig
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.addView
import com.arsvechkarev.vault.viewdsl.size
import com.arsvechkarev.vault.viewdsl.tag
import com.arsvechkarev.vault.viewdsl.withViewBuilder
import com.arsvechkarev.vault.views.MaterialProgressBar
import com.arsvechkarev.vault.views.SimpleDialog
import navigation.BaseScreen

const val DialogProgressBar = "DialogProgressBar"

val BaseScreen.loadingDialog get() = viewAs<SimpleDialog>(DialogProgressBar)

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