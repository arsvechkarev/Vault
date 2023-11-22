package com.arsvechkarev.vault.test.screens

import android.view.View
import android.widget.TextView
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.views.RecyclerTopClip
import com.arsvechkarev.vault.features.main_list.MainListScreen
import com.arsvechkarev.vault.features.main_list.MainListScreen.Companion.ChooseEntryTypeBottomSheet
import com.arsvechkarev.vault.features.main_list.MainListScreen.Companion.EditTextSearch
import com.arsvechkarev.vault.features.main_list.MainListScreen.Companion.ImageSearchAction
import com.arsvechkarev.vault.features.main_list.MainListScreen.Companion.MainListScreenRoot
import com.arsvechkarev.vault.features.main_list.MainListScreen.Companion.MainTitle
import com.arsvechkarev.vault.features.main_list.recycler.MainListAdapter.Companion.ItemPasswordEntryImage
import com.arsvechkarev.vault.features.main_list.recycler.MainListAdapter.Companion.ItemPasswordEntrySubtitle
import com.arsvechkarev.vault.features.main_list.recycler.MainListAdapter.Companion.ItemPasswordEntryTitle
import com.arsvechkarev.vault.features.main_list.recycler.MainListAdapter.Companion.ItemPlainTextTitle
import com.arsvechkarev.vault.test.core.base.BaseScreen
import com.arsvechkarev.vault.test.core.ext.withClassNameTag
import com.arsvechkarev.vault.test.core.views.dialog.KEntryTypeDialog
import com.arsvechkarev.vault.test.core.views.dialog.KInfoDialog
import com.arsvechkarev.vault.test.core.views.menu.KMenuView
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object KMainListScreen : BaseScreen<KMainListScreen>() {
  
  override val viewClass = MainListScreen::class.java
  
  val title = KTextView { withId(MainTitle) }
  val imageSearchAction = KImageView { withId(ImageSearchAction) }
  val editTextSearch = KEditText { withId(EditTextSearch) }
  val menu = KMenuView()
  val entryTypeDialog = KEntryTypeDialog(ChooseEntryTypeBottomSheet)
  val infoDialog = KInfoDialog(MainListScreenRoot)
  
  val recycler = KRecyclerView(
    builder = { withClassNameTag<RecyclerTopClip>() },
    itemTypeBuilder = {
      itemType(KMainListScreen::TitleItem)
      itemType(KMainListScreen::PasswordItem)
      itemType(KMainListScreen::PlainTextItem)
      itemType(KMainListScreen::EmptyItem)
      itemType(KMainListScreen::EmptySearchItem)
    })
  
  class TitleItem(parent: Matcher<View>) : KRecyclerItem<TitleItem>(parent) {
    val title = KTextView {
      withMatcher(parent)
      isInstanceOf(TextView::class.java)
    }
  }
  
  class PasswordItem(parent: Matcher<View>) : KRecyclerItem<PasswordItem>(parent) {
    val image = KImageView(parent) { withId(ItemPasswordEntryImage) }
    val title = KTextView(parent) { withId(ItemPasswordEntryTitle) }
    val subtitle = KTextView(parent) { withId(ItemPasswordEntrySubtitle) }
  }
  
  class PlainTextItem(parent: Matcher<View>) : KRecyclerItem<PlainTextItem>(parent) {
    val title = KTextView(parent) { withId(ItemPlainTextTitle) }
  }
  
  class EmptyItem(parent: Matcher<View>) : KRecyclerItem<EmptyItem>(parent) {
    val image = KImageView(parent) { withDrawable(R.drawable.ic_lists) }
    val title = KTextView(parent) { withText("Nothing yet") }
    val message = KTextView(parent) {
      withText("Click on the + button below to create new entry")
    }
  }
  
  class EmptySearchItem(parent: Matcher<View>) : KRecyclerItem<EmptySearchItem>(parent) {
    val textNoMatchingEntries = KTextView { withText("No matching entries") }
  }
}
