package com.arsvechkarev.vault.features.info

import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity.BOTTOM
import android.view.Gravity.CENTER
import android.view.Gravity.CENTER_HORIZONTAL
import android.view.Gravity.END
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.extensions.getDeleteMessageText
import com.arsvechkarev.vault.core.model.PasswordInfoItem
import com.arsvechkarev.vault.core.setServiceIcon
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens
import com.arsvechkarev.vault.viewbuilding.Dimens.DividerHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.HorizontalMarginPasswordsActionView
import com.arsvechkarev.vault.viewbuilding.Dimens.HorizontalMarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageServiceNameSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginExtraLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginTiny
import com.arsvechkarev.vault.viewbuilding.Dimens.VerticalMarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ImageBack
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.views.EditableTextInfoViewGroup
import com.arsvechkarev.vault.views.PasswordActionsView
import com.arsvechkarev.vault.views.Snackbar
import com.arsvechkarev.vault.views.dialogs.InfoDialog.Companion.InfoDialog
import com.arsvechkarev.vault.views.dialogs.InfoDialog.Companion.infoDialog
import com.arsvechkarev.vault.views.dialogs.LoadingDialog
import com.arsvechkarev.vault.views.dialogs.loadingDialog
import navigation.BaseScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.Size.IntSize
import viewdsl.animateInvisible
import viewdsl.animateVisible
import viewdsl.backgroundColor
import viewdsl.circleRippleBackground
import viewdsl.classNameTag
import viewdsl.gravity
import viewdsl.image
import viewdsl.invisible
import viewdsl.layoutGravity
import viewdsl.margin
import viewdsl.marginHorizontal
import viewdsl.margins
import viewdsl.padding
import viewdsl.tag
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.withViewBuilder

class InfoScreen : BaseScreen(), InfoView {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootCoordinatorLayout {
      ScrollableVerticalLayout {
        backgroundColor(Colors.Background)
        gravity(CENTER_HORIZONTAL)
        margins(top = StatusBarHeight)
        FrameLayout(MatchParent, WrapContent) {
          margins(top = MarginNormal, start = MarginNormal, end = MarginNormal)
          ImageView(WrapContent, WrapContent, style = ImageBack) {
            //            onClick { presenter.onBackClicked() }
          }
          ImageView(WrapContent, WrapContent) {
            image(R.drawable.ic_delete)
            imageTintList = ColorStateList.valueOf(Colors.Error)
            padding(Dimens.IconPadding)
            circleRippleBackground(Colors.ErrorRipple)
            layoutGravity(END)
            //            onClick { if (!presenter.isEditingNameOrEmailNow) presenter.onDeleteClicked() }
          }
        }
        ImageView(ImageServiceNameSize, ImageServiceNameSize) {
          tag(ImageServiceName)
        }
        TextView(WrapContent, WrapContent, style = BaseTextView) {
//          text(R.string.text_password_name)
          margins(top = MarginExtraLarge)
          textColor(Colors.TextSecondary)
        }
        val editableCommonBlock: EditableTextInfoViewGroup.() -> Unit = {
          marginHorizontal(HorizontalMarginPasswordsActionView)
          //          onEditClickAllowed = { !presenter.isEditingNameOrEmailNow }
          //          onSwitchToEditMode = { presenter.switchToEditingMode() }
        }
        child<EditableTextInfoViewGroup>(MatchParent, WrapContent) {
          tag(EditableTextInfoServiceName)
          apply(editableCommonBlock)
          //          onEditTextChanged { presenter.onServiceNameChanged(it) }
          //          onTextSaved = { presenter.saveServiceName(it) }
        }
        View(MatchParent, IntSize(DividerHeight)) {
          backgroundColor(Colors.Divider)
          marginHorizontal(HorizontalMarginSmall)
          margins(top = VerticalMarginSmall)
        }
        TextView(WrapContent, WrapContent, style = BaseTextView) {
          margins(top = VerticalMarginSmall)
          textColor(Colors.TextSecondary)
          text(R.string.text_login)
        }
        child<EditableTextInfoViewGroup>(MatchParent, WrapContent) {
          tag(EditableTextInfoUsername)
          apply(editableCommonBlock)
          allowSavingWhenEmpty = true
          //          onTextSaved = { presenter.saveUsername(it) }
        }
        View(MatchParent, IntSize(DividerHeight)) {
          backgroundColor(Colors.Divider)
          marginHorizontal(HorizontalMarginSmall)
          margins(top = VerticalMarginSmall)
        }
        TextView(WrapContent, WrapContent, style = BaseTextView) {
          margins(top = VerticalMarginSmall)
          textColor(Colors.TextSecondary)
          text(R.string.text_email)
        }
        child<EditableTextInfoViewGroup>(MatchParent, WrapContent) {
          tag(EditableTextInfoEmail)
          apply(editableCommonBlock)
          allowSavingWhenEmpty = true
          //          onTextSaved = { presenter.saveEmail(it) }
        }
        View(MatchParent, IntSize(DividerHeight)) {
          backgroundColor(Colors.Divider)
          marginHorizontal(HorizontalMarginSmall)
          margins(top = VerticalMarginSmall)
        }
        TextView(WrapContent, WrapContent, style = BaseTextView) {
          text(R.string.text_password)
          margins(top = VerticalMarginSmall, bottom = MarginTiny)
          textColor(Colors.TextSecondary)
          textSize(TextSizes.H4)
        }
        FrameLayout(WrapContent, WrapContent) {
          marginHorizontal(HorizontalMarginSmall)
          TextView(WrapContent, WrapContent, style = BoldTextView) {
            tag(TextPassword)
            invisible()
            layoutGravity(CENTER)
            gravity(CENTER)
            textSize(TextSizes.H3)
            textColor(Colors.AccentLight)
          }
          TextView(WrapContent, WrapContent, style = BoldTextView) {
            tag(TextPasswordStub)
            textSize(TextSizes.PasswordStub)
            layoutGravity(CENTER)
            gravity(CENTER)
            text(R.string.text_password_stub)
          }
        }
        child<PasswordActionsView>(WrapContent, WrapContent) {
          classNameTag()
          margins(top = VerticalMarginSmall, start = HorizontalMarginPasswordsActionView,
            end = HorizontalMarginPasswordsActionView)
          //          onCopyClicked { presenter.onCopyClicked() }
          //          onEditClicked { presenter.onEditPasswordIconClicked() }
          //          onTogglePassword = { presenter.onTogglePassword(it) }
          //          reactToClicks = { !presenter.isEditingNameOrEmailNow }
        }
      }
      LoadingDialog()
      InfoDialog()
      child<Snackbar>(MatchParent, WrapContent) {
        classNameTag()
        layoutGravity(BOTTOM)
        margin(MarginNormal)
      }
    }
  }
  
  //  private val presenter by moxyPresenter {
  //    CoreComponent.instance.getInfoComponentFactory().create().providePresenter()
  //  }
  
  override fun onAppearedOnScreenGoingForward() {
    val serviceInfo = arguments[SERVICE] as PasswordInfoItem
    //    presenter.performSetup(serviceInfo)
  }
  
  override fun showServiceName(serviceName: String) {
    viewAs<EditableTextInfoViewGroup>(EditableTextInfoServiceName).setText(serviceName)
  }
  
  override fun showServiceIcon(serviceName: String) {
    imageView(ImageServiceName).setServiceIcon(serviceName)
  }
  
  override fun showUsername(username: String) {
    val editableUsername = viewAs<EditableTextInfoViewGroup>(EditableTextInfoUsername)
    editableUsername.setText(username)
    editableUsername.transferTextToEditTextWhenSwitching = true
  }
  
  override fun showNoUsername() {
    val editableUsername = viewAs<EditableTextInfoViewGroup>(EditableTextInfoUsername)
    editableUsername.setText(contextNonNull.getString(R.string.text_no_username))
    editableUsername.transferTextToEditTextWhenSwitching = false
  }
  
  override fun showEmail(email: String) {
    val editableEmail = viewAs<EditableTextInfoViewGroup>(EditableTextInfoEmail)
    editableEmail.setText(email)
    editableEmail.transferTextToEditTextWhenSwitching = true
  }
  
  override fun showNoEmail() {
    val editableEmail = viewAs<EditableTextInfoViewGroup>(EditableTextInfoEmail)
    editableEmail.setText(contextNonNull.getString(R.string.text_no_email))
    editableEmail.transferTextToEditTextWhenSwitching = false
  }
  
  override fun setPassword(password: String) {
    textView(TextPassword).text(password)
  }
  
  override fun restoreInitialData() {
    viewAs<EditableTextInfoViewGroup>(EditableTextInfoServiceName).cancelEditing()
    viewAs<EditableTextInfoViewGroup>(EditableTextInfoUsername).cancelEditing()
    viewAs<EditableTextInfoViewGroup>(EditableTextInfoEmail).cancelEditing()
  }
  
  override fun showPassword(password: String) {
    textView(TextPassword).text(password)
    textView(TextPassword).animateVisible()
    textView(TextPasswordStub).animateInvisible()
  }
  
  override fun hidePassword() {
    viewAs<PasswordActionsView>().showOpenedEye()
    textView(TextPassword).animateInvisible()
    textView(TextPasswordStub).animateVisible()
  }
  
  override fun showDeleteDialog(serviceName: String) {
    //    infoDialog.onHide = { presenter.onHideDeleteDialog() }
    infoDialog.showWithDeleteAndCancelOption(
      R.string.text_delete_service, getDeleteMessageText(serviceName),
      onDeleteClicked = { }
    )
  }
  
  override fun hideDeleteDialog() {
    infoDialog.onHide = {}
    infoDialog.hide()
  }
  
  override fun showLoading() {
    loadingDialog.show()
  }
  
  override fun hideLoading() {
    loadingDialog.hide()
  }
  
  override fun showCopiedPassword() {
    viewAs<Snackbar>().show(R.string.text_password_copied)
  }
  
  override fun showExit() {
    loadingDialog.hide()
  }
  
  override fun handleBackPress(): Boolean {
    return false
    //    return presenter.handleBackPress()
  }
  
  companion object {
  
    const val SERVICE = "SERVICE"
    
    const val ImageServiceName = "ImageServiceName"
    const val EditableTextInfoServiceName = "EditableTextInfoServiceName"
    const val EditableTextInfoUsername = "EditableTextInfoUsername"
    const val EditableTextInfoEmail = "EditableTextInfoEmail"
    const val TextPassword = "TextPassword"
    const val TextPasswordStub = "TextPasswordStub"
  }
}
