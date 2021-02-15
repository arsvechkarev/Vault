package com.arsvechkarev.vault.features.passwords_list

import android.view.Gravity.CENTER
import android.widget.ImageView
import android.widget.TextView
import com.arsvechkarev.vault.core.model.ServiceInfo
import com.arsvechkarev.vault.features.common.getIconForServiceName
import com.arsvechkarev.vault.recycler.CallbackType
import com.arsvechkarev.vault.recycler.ListAdapter
import com.arsvechkarev.vault.recycler.delegate
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.ItemServiceInfoImageSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.image
import com.arsvechkarev.vault.viewdsl.layoutGravity
import com.arsvechkarev.vault.viewdsl.margins
import com.arsvechkarev.vault.viewdsl.onClick
import com.arsvechkarev.vault.viewdsl.onLongClick
import com.arsvechkarev.vault.viewdsl.padding
import com.arsvechkarev.vault.viewdsl.rippleBackground
import com.arsvechkarev.vault.viewdsl.tag
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.viewAs
import com.arsvechkarev.vault.views.drawables.LetterInCircleDrawable.Companion.setLetterDrawable

class PasswordsListAdapter(
  private val onItemClick: (ServiceInfo) -> Unit,
  private val onItemLongClick: (ServiceInfo) -> Unit
) : ListAdapter() {
  
  init {
    addDelegates(
      delegate<ServiceInfo> {
        buildView {
          RootHorizontalLayout(MatchParent, WrapContent) {
            rippleBackground(Colors.Ripple)
            padding(MarginSmall)
            ImageView(ItemServiceInfoImageSize, ItemServiceInfoImageSize) {
              tag(ItemServiceInfoImage)
              layoutGravity(CENTER)
              margins(MarginDefault)
            }
            TextView(WrapContent, WrapContent, style = BoldTextView) {
              tag(ItemServiceInfoTextServiceName)
              margins(MarginDefault)
              layoutGravity(CENTER)
            }
          }
        }
        onInitViewHolder {
          itemView.onClick { onItemClick(item) }
          itemView.onLongClick { onItemLongClick(item) }
        }
        onBind {
          val icon = getIconForServiceName(item.name)
          if (icon != null) {
            itemView.viewAs<ImageView>(ItemServiceInfoImage).image(icon)
          } else {
            val letter = item.name[0].toString()
            itemView.viewAs<ImageView>(ItemServiceInfoImage).setLetterDrawable(letter)
          }
          itemView.viewAs<TextView>(ItemServiceInfoTextServiceName).text(item.name)
        }
      }
    )
  }
  
  fun removeItem(serviceInfo: ServiceInfo) {
    val list = ArrayList(data)
    list.remove(serviceInfo)
    submitList(list, CallbackType.TWO_LISTS)
  }
  
  private companion object {
    
    const val ItemServiceInfoImage = "ItemServiceInfoImage"
    const val ItemServiceInfoTextServiceName = "ItemServiceInfoTextServiceName"
  }
}