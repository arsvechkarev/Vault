package com.arsvechkarev.vault.test.features.main_list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.main_list.MainListScreen
import com.arsvechkarev.vault.features.main_list.recycler.MainListAdapter.Companion.ItemPasswordInfoImage
import com.arsvechkarev.vault.features.main_list.recycler.MainListAdapter.Companion.ItemPasswordInfoTextPasswordName
import com.arsvechkarev.vault.test.core.base.BaseScreen
import com.arsvechkarev.vault.test.core.ext.withClassNameTag
import com.arsvechkarev.vault.test.core.views.menu.KMenuView
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object KMainListScreen : BaseScreen<KMainListScreen>() {
  
  override val viewClass = MainListScreen::class.java
  
  val menu = KMenuView()
  
  val recycler = KRecyclerView(
    builder = { withClassNameTag<RecyclerView>() },
    itemTypeBuilder = {
      itemType(::PasswordItem)
      itemType(::EmptyItem)
    })
  
  class PasswordItem(parent: Matcher<View>) : KRecyclerItem<PasswordItem>(parent) {
    val icon = KImageView(parent) { withId(ItemPasswordInfoImage) }
    val text = KTextView(parent) { withId(ItemPasswordInfoTextPasswordName) }
  }
  
  class EmptyItem(parent: Matcher<View>) : KRecyclerItem<EmptyItem>(parent) {
    val image = KImageView(parent) { withDrawable(R.drawable.ic_lists) }
    val title = KTextView(parent) { withText("No passwords saved") }
    val message = KTextView(parent) {
      withText("Click on the + button below to create new password")
    }
  }
}
