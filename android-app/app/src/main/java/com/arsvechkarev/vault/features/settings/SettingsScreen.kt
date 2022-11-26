package com.arsvechkarev.vault.features.settings

import android.content.Context
import android.view.Gravity
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.common.dialogs.CheckMasterPasswordDialog
import com.arsvechkarev.vault.features.common.dialogs.CheckMasterPasswordDialog.Companion.CheckMasterPasswordDialog
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Styles
import com.arsvechkarev.vault.viewbuilding.Styles.ImageBack
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.views.behaviors.BottomSheetBehavior.Companion.asBottomSheet
import navigation.BaseFragmentScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.constraints
import viewdsl.gravity
import viewdsl.id
import viewdsl.layoutGravity
import viewdsl.margin
import viewdsl.margins
import viewdsl.onClick
import viewdsl.text
import viewdsl.textSize
import viewdsl.withViewBuilder

class SettingsScreen : BaseFragmentScreen() {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootCoordinatorLayout {
      child<ConstraintLayout>(MatchParent, WrapContent) {
        HorizontalLayout(MatchParent, WrapContent) {
          id(ToolbarId)
          margins(top = StatusBarHeight)
          constraints {
            topToTopOf(parent)
          }
          ImageView(WrapContent, WrapContent, style = ImageBack) {
            margin(MarginNormal)
            gravity(Gravity.CENTER_VERTICAL)
            onClick { viewAs<CheckMasterPasswordDialog>().asBottomSheet.show() }
            //            onClick { store.tryDispatch(CreatingEntryUiEvent.OnBackButtonClicked) }
          }
          TextView(WrapContent, WrapContent, style = Styles.BoldTextView) {
            layoutGravity(Gravity.CENTER)
            text(R.string.text_settings)
            textSize(TextSizes.H1)
          }
        }
      }
      CheckMasterPasswordDialog(
        onCheckSuccessful = { println("checkSuccessfulll") },
        onDialogClosed = { println("dialogClosed") }
      )
    }
  }
  
  private companion object {
  
    val ToolbarId = View.generateViewId()
  }
}
