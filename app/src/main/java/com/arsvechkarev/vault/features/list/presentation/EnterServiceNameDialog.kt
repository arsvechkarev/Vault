package com.arsvechkarev.vault.features.list.presentation

import android.content.Context
import android.widget.EditText
import android.widget.TextView
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.core.extensions.BaseTextWatcher
import com.arsvechkarev.vault.core.extensions.hideKeyboard
import com.arsvechkarev.vault.core.extensions.showKeyboard
import com.arsvechkarev.vault.core.model.ServiceInfo
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Fonts
import com.arsvechkarev.vault.viewbuilding.Styles
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewdsl.Ints.dp
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.backgroundRoundRect
import com.arsvechkarev.vault.viewdsl.childViewAs
import com.arsvechkarev.vault.viewdsl.font
import com.arsvechkarev.vault.viewdsl.invisible
import com.arsvechkarev.vault.viewdsl.marginHorizontal
import com.arsvechkarev.vault.viewdsl.margins
import com.arsvechkarev.vault.viewdsl.onClick
import com.arsvechkarev.vault.viewdsl.padding
import com.arsvechkarev.vault.viewdsl.paddingHorizontal
import com.arsvechkarev.vault.viewdsl.paddingVertical
import com.arsvechkarev.vault.viewdsl.size
import com.arsvechkarev.vault.viewdsl.tag
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textColor
import com.arsvechkarev.vault.viewdsl.textSize
import com.arsvechkarev.vault.viewdsl.withViewBuilder
import com.arsvechkarev.vault.views.SimpleDialog

class EnterServiceNameDialog(
  context: Context,
  private val threader: Threader,
) : SimpleDialog(context) {
  
  private val editText get() = childViewAs<EditText>(EnterServiceNameEditText)
  private val textError get() = childViewAs<TextView>(EnterServiceNameTextError)
  private var servicesInfoList: List<ServiceInfo>? = null
  private var onReady: (serviceName: String) -> Unit = {}
  
  private val passwordTextWatcher = object : BaseTextWatcher {
    
    override fun onTextChange(text: String) = textError.text("")
  }
  
  init {
    onHide = {
      editText.text.clear()
      context.hideKeyboard(editText)
      textError.text("")
    }
    withViewBuilder {
      tag(EnterServiceNameDialog)
      size(MatchParent, MatchParent)
      VerticalLayout(MatchParent, WrapContent) {
        marginHorizontal(30.dp)
        paddingHorizontal(16.dp)
        paddingVertical(24.dp)
        backgroundRoundRect(8.dp, Colors.Dialog)
        TextView(WrapContent, WrapContent, style = Styles.BoldTextView) {
          marginHorizontal(8.dp)
          textSize(TextSizes.H3)
          text(context.getString(R.string.text_enter_service_name))
        }
        EditText(MatchParent, WrapContent) {
          tag(EnterServiceNameEditText)
          margins(top = 42.dp)
          font(Fonts.SegoeUi)
          textSize(TextSizes.H3)
          padding(8.dp)
          setHint(R.string.text_service_name)
          setSingleLine()
        }
        TextView(WrapContent, WrapContent, style = Styles.BaseTextView) {
          tag(EnterServiceNameTextError)
          invisible()
          margins(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 32.dp)
          textColor(Colors.Error)
        }
        TextView(MatchParent, WrapContent, style = Styles.ClickableButton()) {
          tag(EnterServiceNameContinueButton)
          text(R.string.text_next)
          marginHorizontal(16.dp)
          onClick(::checkServiceName)
        }
      }
    }
  }
  
  fun showEnterServiceName(serviceNamesList: List<ServiceInfo>, onReady: (serviceName: String) -> Unit) {
    this.servicesInfoList = serviceNamesList
    this.onReady = onReady
    show()
    context.showKeyboard()
    editText.requestFocus()
  }
  
  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    editText.addTextChangedListener(passwordTextWatcher)
  }
  
  override fun onDetachedFromWindow() {
    editText.removeTextChangedListener(passwordTextWatcher)
    onReady = {}
    super.onDetachedFromWindow()
  }
  
  private fun checkServiceName() {
    val serviceName = editText.text.toString()
    if (serviceName.isBlank()) {
      textError.text(context.getString(R.string.text_name_cannot_be_blank))
      return
    }
    threader.onBackgroundThread {
      for (serviceInfo in servicesInfoList!!) {
        if (serviceInfo.serviceName == serviceName) {
          textError.text(context.getString(R.string.text_service_already_exists))
          return@onBackgroundThread
        }
      }
      threader.onMainThread {
        hide()
        context.hideKeyboard(editText)
        onReady(serviceName)
      }
    }
  }
  
  companion object {
    
    const val EnterServiceNameDialog = "EnterServiceNameDialog"
    const val EnterServiceNameTextError = "EnterServiceNameTextError"
    const val EnterServiceNameEditText = "EnterServiceNameEditText"
    const val EnterServiceNameContinueButton = "EnterServiceNameContinueButton"
  }
}