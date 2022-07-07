package com.arsvechkarev.vault.features.settings

import android.content.Context
import android.view.View
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.mvi.MviView
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingDialog.Companion.PasswordCheckingDialog
import com.arsvechkarev.vault.features.settings.SettingsScreenSingleEvents.ShowBiometricsAddedSuccessfully
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.VerticalMarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ImageBack
import com.arsvechkarev.vault.viewbuilding.Styles.TitleTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.views.Snackbar
import navigation.BaseScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.backgroundColor
import viewdsl.constraints
import viewdsl.id
import viewdsl.margin
import viewdsl.margins
import viewdsl.paddings
import viewdsl.rippleBackground
import viewdsl.setCheckedSafe
import viewdsl.text
import viewdsl.textSize
import viewdsl.withViewBuilder

class SettingsScreen : BaseScreen(), MviView<SettingsScreenState, SettingsScreenSingleEvents> {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootConstraintLayout {
      id(RootLayoutId)
      backgroundColor(Colors.Background)
      ImageView(WrapContent, WrapContent, style = ImageBack) {
        id(ImageBackId)
        margins(start = MarginNormal)
        constraints {
          startToStartOf(parent)
          topToTopOf(TextTitleId)
          bottomToBottomOf(TextTitleId)
        }
        //        onClick { presenter.applyAction(OnBackPressed) }
      }
      TextView(WrapContent, WrapContent, style = TitleTextView) {
        id(TextTitleId)
        margins(top = MarginNormal + StatusBarHeight, start = MarginNormal)
        constraints {
          topToTopOf(parent)
          startToEndOf(ImageBackId)
        }
        text(R.string.text_settings)
      }
      child<ConstraintLayout>(MatchParent, WrapContent) {
        id(LayoutUseFingerprintId)
        margins(top = VerticalMarginSmall)
        constraints {
          startToStartOf(parent)
          topToBottomOf(TextTitleId)
        }
        //        onClick { presenter.applyAction(OnUserFingerprintTextClicked) }
        rippleBackground(Colors.Ripple)
        TextView(MatchParent, WrapContent, style = BaseTextView) {
          id(TextUseFingerprintId)
          paddings(top = MarginNormal, bottom = MarginNormal, start = MarginNormal)
          constraints {
            startToStartOf(parent)
            endToStartOf(SwitchUseFingerprintId)
            topToTopOf(SwitchUseFingerprintId)
            bottomToBottomOf(SwitchUseFingerprintId)
          }
          textSize(TextSizes.H4)
          text("Use fingerprint for entering the app")
        }
        child<SwitchCompat>(WrapContent, WrapContent) {
          id(SwitchUseFingerprintId)
          margins(end = MarginNormal)
          constraints {
            topToTopOf(parent)
            bottomToBottomOf(parent)
            endToEndOf(parent)
          }
          setOnCheckedChangeListener { _, isChecked ->
            //            presenter.applyAction(ToggleUseFingerprintForEnteringCheckbox(isChecked))
          }
        }
      }
      PasswordCheckingDialog {
        id(PasswordCheckingDialogId)
        //        onHide = { presenter.applyAction(HidePasswordCheckingDialog) }
      }
      child<Snackbar>(MatchParent, WrapContent) {
        id(SnackbarId)
        margin(MarginNormal)
        constraints {
          bottomToBottomOf(parent)
        }
      }
    }
  }
  
  override fun render(state: SettingsScreenState) {
    viewAs<SwitchCompat>(SwitchUseFingerprintId).setCheckedSafe(state.fingerprintEnteringEnabled)
  }
  
  override fun handleNews(event: SettingsScreenSingleEvents) {
    if (event is ShowBiometricsAddedSuccessfully) {
      viewAs<Snackbar>(SnackbarId).show(R.string.text_biometrics_added)
    }
  }
  
  override fun handleBackPress(): Boolean {
    //    presenter.applyAction(OnBackPressed)
    return true
  }
  
  private companion object {
    
    val RootLayoutId = View.generateViewId()
    val ImageBackId = View.generateViewId()
    val TextTitleId = View.generateViewId()
    val LayoutUseFingerprintId = View.generateViewId()
    val TextUseFingerprintId = View.generateViewId()
    val SwitchUseFingerprintId = View.generateViewId()
    val PasswordCheckingDialogId = View.generateViewId()
    val SnackbarId = View.generateViewId()
  }
}