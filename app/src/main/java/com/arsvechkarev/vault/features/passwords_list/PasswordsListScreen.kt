package com.arsvechkarev.vault.features.passwords_list

import android.view.Gravity.BOTTOM
import android.view.Gravity.CENTER
import android.view.Gravity.END
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.AndroidThreader
import com.arsvechkarev.vault.core.Singletons.passwordsListRepository
import com.arsvechkarev.vault.core.extensions.getDeleteMessageText
import com.arsvechkarev.vault.core.extensions.ifTrue
import com.arsvechkarev.vault.core.extensions.moxyPresenter
import com.arsvechkarev.vault.core.model.ServiceInfo
import com.arsvechkarev.vault.core.navigation.Screen
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.FabSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.ProgressBarSizeBig
import com.arsvechkarev.vault.viewbuilding.Dimens.VerticalMarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
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
import com.arsvechkarev.vault.viewdsl.paddings
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
import com.arsvechkarev.vault.views.dialogs.InfoDialog.Companion.InfoDialog
import com.arsvechkarev.vault.views.dialogs.InfoDialog.Companion.infoDialog
import com.arsvechkarev.vault.views.dialogs.LoadingDialog
import com.arsvechkarev.vault.views.dialogs.loadingDialog

class PasswordsListScreen : Screen(), PasswordsListView {
  
  override fun buildLayout() = withViewBuilder {
    val viewUnderHeaderBehavior = ViewUnderHeaderBehavior()
    RootCoordinatorLayout {
      TextView(MatchParent, WrapContent, style = BoldTextView) {
        paddings(top = VerticalMarginSmall + StatusBarHeight, bottom = MarginSmall,
          start = MarginDefault)
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
        layoutGravity(CENTER)
        gravity(CENTER)
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
        layoutGravity(CENTER)
        gravity(CENTER)
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          text("No passwords saved")
        }
      }
      ImageView(FabSize, FabSize) {
        margin(MarginDefault)
        padding(MarginSmall)
        image(R.drawable.ic_plus)
        layoutGravity(BOTTOM or END)
        rippleBackground(Colors.Ripple, Colors.Accent, FabSize)
        onClick { navigator.goToNewServiceScreen() }
      }
      InfoDialog()
      LoadingDialog()
    }
  }
  
  private val adapter by lazy {
    PasswordsListAdapter(
      onItemClick = navigator::goToSavedServiceInfoScreen,
      onItemLongClick = presenter::onLongClick
    )
  }
  
  private val presenter by moxyPresenter {
    PasswordsListPresenter(AndroidThreader, passwordsListRepository)
  }
  
  override fun onInit() {
    viewAs<RecyclerView>().setupWith(adapter)
    presenter.startLoadingPasswords()
  }
  
  override fun showLoading() {
    showView(view(LayoutLoading))
  }
  
  override fun showNoPasswords() {
    showView(view(LayoutNoPasswords))
  }
  
  override fun showPasswordsList(list: List<ServiceInfo>) {
    showView(viewAs<RecyclerView>())
    adapter.changeListWithoutAnimation(list)
  }
  
  override fun showDeleteDialog(serviceInfo: ServiceInfo) {
    infoDialog.showWithDeleteAndCancelOption(
      R.string.text_delete_service, getDeleteMessageText(serviceInfo.serviceName),
      onDeleteClicked = { presenter.deleteService(serviceInfo) }
    )
  }
  
  override fun showLoadingDeletingService() {
    infoDialog.hide()
    loadingDialog.show()
  }
  
  override fun showDeletedService(serviceInfo: ServiceInfo) {
    adapter.removeItem(serviceInfo)
    loadingDialog.hide()
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