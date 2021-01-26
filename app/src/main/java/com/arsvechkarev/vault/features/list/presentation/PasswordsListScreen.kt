package com.arsvechkarev.vault.features.list.presentation

import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.AndroidThreader
import com.arsvechkarev.vault.core.extensions.ifTrue
import com.arsvechkarev.vault.core.extensions.moxyPresenter
import com.arsvechkarev.vault.core.model.ServiceInfo
import com.arsvechkarev.vault.core.navigation.Screen
import com.arsvechkarev.vault.features.list.domain.PasswordsListRepository
import com.arsvechkarev.vault.password.MasterPasswordHolder
import com.arsvechkarev.vault.password.PasswordsSaverImpl
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.FabSize
import com.arsvechkarev.vault.viewbuilding.Dimens.ProgressBarSizeBig
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewdsl.Ints.dp
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.addView
import com.arsvechkarev.vault.viewdsl.animateInvisible
import com.arsvechkarev.vault.viewdsl.animateVisible
import com.arsvechkarev.vault.viewdsl.behavior
import com.arsvechkarev.vault.viewdsl.classNameTag
import com.arsvechkarev.vault.viewdsl.gravity
import com.arsvechkarev.vault.viewdsl.image
import com.arsvechkarev.vault.viewdsl.invisible
import com.arsvechkarev.vault.viewdsl.layoutGravity
import com.arsvechkarev.vault.viewdsl.margin
import com.arsvechkarev.vault.viewdsl.onClick
import com.arsvechkarev.vault.viewdsl.padding
import com.arsvechkarev.vault.viewdsl.rippleBackground
import com.arsvechkarev.vault.viewdsl.setupWith
import com.arsvechkarev.vault.viewdsl.size
import com.arsvechkarev.vault.viewdsl.tag
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textSize
import com.arsvechkarev.vault.views.MaterialProgressBar
import com.arsvechkarev.vault.views.behaviors.HeaderBehavior
import com.arsvechkarev.vault.views.behaviors.ScrollingRecyclerBehavior
import com.arsvechkarev.vault.views.behaviors.ViewUnderHeaderBehavior

class PasswordsListScreen : Screen(), PasswordsListView {
  
  override fun buildLayout() = withViewBuilder {
    val viewUnderHeaderBehavior = ViewUnderHeaderBehavior()
    RootCoordinatorLayout {
      TextView(MatchParent, WrapContent, style = BoldTextView) {
        margin(24.dp)
        textSize(TextSizes.H0)
        behavior(HeaderBehavior())
        text(getString(R.string.text_passwords))
      }
      RecyclerView(MatchParent, MatchParent) {
        classNameTag()
        behavior(ScrollingRecyclerBehavior())
      }
      VerticalLayout(MatchParent, MatchParent) {
        tag(LayoutLoading)
        invisible()
        behavior(viewUnderHeaderBehavior)
        layoutGravity(Gravity.CENTER)
        gravity(Gravity.CENTER)
        addView {
          MaterialProgressBar(context).apply {
            size(ProgressBarSizeBig, ProgressBarSizeBig)
          }
        }
      }
      VerticalLayout(MatchParent, MatchParent) {
        tag(LayoutNoPasswords)
        invisible()
        behavior(viewUnderHeaderBehavior)
        layoutGravity(Gravity.CENTER)
        gravity(Gravity.CENTER)
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          text("No passwords saved")
        }
      }
      ImageView(FabSize, FabSize) {
        margin(16.dp)
        padding(6.dp)
        image(R.drawable.ic_plus)
        layoutGravity(Gravity.BOTTOM or Gravity.END)
        rippleBackground(Colors.Ripple, Colors.Accent, FabSize)
        onClick { presenter.onNewServiceButtonClick() }
      }
      addView {
        EnterServiceNameDialog(context, AndroidThreader).apply {
          classNameTag()
        }
      }
    }
  }
  
  private val adapter = PasswordsListAdapter(onItemClick = { passwordInfo ->
  
  })
  
  private val presenter by moxyPresenter {
    PasswordsListPresenter(
      AndroidThreader,
      PasswordsListRepository(PasswordsSaverImpl(contextNonNull))
    )
  }
  
  override fun onInit() {
    viewAs<RecyclerView>().setupWith(adapter)
  }
  
  override fun onAppearedOnScreen() {
    presenter.loadPasswords(MasterPasswordHolder.masterPassword)
  }
  
  override fun showLoading() {
    showView(view(LayoutLoading))
  }
  
  override fun showNoPasswords() {
    showView(view(LayoutNoPasswords))
  }
  
  override fun showPasswordsList(list: List<ServiceInfo>) {
    showView(viewAs<RecyclerView>())
    adapter.submitList(list)
  }
  
  override fun showEnterServiceNameDialog(servicesInfoList: List<ServiceInfo>) {
    viewAs<EnterServiceNameDialog>().showEnterServiceName(servicesInfoList,
      onReady = { newServiceName ->
        navigator.goToNewPasswordScreen(newServiceName)
      })
  }
  
  private fun showView(layout: View) {
    viewAs<RecyclerView>().ifTrue({ it !== layout }, { animateInvisible() })
    view(LayoutLoading).ifTrue({ it !== layout }, { animateInvisible() })
    view(LayoutNoPasswords).ifTrue({ it !== layout }, { animateInvisible() })
    layout.animateVisible()
  }
  
  private companion object {
    
    const val LayoutLoading = "LayoutLoading"
    const val LayoutNoPasswords = "LayoutNoPasswords"
  }
}