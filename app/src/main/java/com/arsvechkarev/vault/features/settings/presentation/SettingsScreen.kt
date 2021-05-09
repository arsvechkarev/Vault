package com.arsvechkarev.vault.features.settings.presentation

import android.content.Context
import android.view.View
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.di.CoreComponent
import com.arsvechkarev.vault.core.extensions.moxyPresenter
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingDialog.Companion.PasswordCheckingDialog
import com.arsvechkarev.vault.features.password_checking.PasswordCheckingDialog.Companion.passwordCheckingDialog
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.VerticalMarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ImageBack
import com.arsvechkarev.vault.viewbuilding.Styles.TitleTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.backgroundColor
import com.arsvechkarev.vault.viewdsl.constraints
import com.arsvechkarev.vault.viewdsl.id
import com.arsvechkarev.vault.viewdsl.margins
import com.arsvechkarev.vault.viewdsl.onClick
import com.arsvechkarev.vault.viewdsl.paddings
import com.arsvechkarev.vault.viewdsl.rippleBackground
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textSize
import com.arsvechkarev.vault.viewdsl.withViewBuilder
import navigation.BaseScreen

class SettingsScreen : BaseScreen(), SettingsView {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootConstraintLayout {
      id(RootLayoutId)
      backgroundColor(Colors.Background)
      ImageView(WrapContent, WrapContent, style = ImageBack) {
        id(ImageBackId)
        margins(start = MarginDefault)
        constraints {
          startToStartOf(parent)
          topToTopOf(TextTitleId)
          bottomToBottomOf(TextTitleId)
        }
        onClick { presenter.onBackClicked() }
      }
      TextView(WrapContent, WrapContent, style = TitleTextView) {
        id(TextTitleId)
        margins(top = MarginDefault + StatusBarHeight, start = MarginDefault)
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
        onClick { presenter.onUseFingerprintsTextClicked() }
        rippleBackground()
        TextView(MatchParent, WrapContent, style = BaseTextView) {
          id(TextUseFingerprintId)
          paddings(top = MarginDefault, bottom = MarginDefault, start = MarginDefault)
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
          margins(end = MarginDefault)
          constraints {
            topToTopOf(parent)
            bottomToBottomOf(parent)
            endToEndOf(parent)
          }
          setOnCheckedChangeListener { _, isChecked ->
            presenter.toggleUseFingerprintForEntering(isChecked)
          }
        }
      }
      PasswordCheckingDialog()
    }
  }
  
  private val presenter by moxyPresenter {
    CoreComponent.instance.getSettingsComponentFactory().create().providePresenter()
  }
  
  override fun showUseFingerprintForEnteringEnabled(checked: Boolean) {
    val switch = viewAs<SwitchCompat>(SwitchUseFingerprintId)
    if (switch.isChecked != checked) {
      switch.isChecked = checked
    }
  }
  
  override fun showPasswordCheckingDialog() {
    passwordCheckingDialog.show()
  }
  
  override fun hidePasswordCheckingDialog() {
    passwordCheckingDialog.hide()
  }
  
  private companion object {
    
    private val RootLayoutId = View.generateViewId()
    private val ImageBackId = View.generateViewId()
    private val TextTitleId = View.generateViewId()
    private val LayoutUseFingerprintId = View.generateViewId()
    private val TextUseFingerprintId = View.generateViewId()
    private val SwitchUseFingerprintId = View.generateViewId()
  }
}