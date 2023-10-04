package com.arsvechkarev.vault.features.main_list.domain

import app.keemobile.kotpass.database.KeePassDatabase
import app.keemobile.kotpass.database.getEntries
import app.keemobile.kotpass.models.Entry
import buisnesslogic.isDefinitePlainText
import buisnesslogic.isFavorite
import buisnesslogic.isProbablePlainText
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.common.model.PasswordItem
import com.arsvechkarev.vault.features.common.model.PlainTextItem
import com.arsvechkarev.vault.features.common.model.Title
import com.arsvechkarev.vault.recycler.DifferentiableItem

class EntriesListUiMapper {
  
  fun mapItems(database: KeePassDatabase, addUsernames: Boolean): List<DifferentiableItem> {
    val allEntries = database.getEntries { true }.flatMap { pair -> pair.second }
    val plainTexts = allEntries.filter { it.isDefinitePlainText || it.isProbablePlainText }
    val groupedPasswords = (allEntries - plainTexts.toSet()).groupBy { it.isFavorite }
    val favoritePasswords = groupedPasswords[true].orEmpty()
    val nonFavoritePasswords = groupedPasswords[false].orEmpty()
    val groupedPlainTexts = plainTexts.groupBy { it.isFavorite }
    val favoritePlainTexts = groupedPlainTexts[true].orEmpty()
    val nonFavoritePlainTexts = groupedPlainTexts[false].orEmpty()
    return buildList {
      if (favoritePasswords.isNotEmpty() || favoritePlainTexts.isNotEmpty()) {
        add(Title(R.string.text_favorites))
        addAll(favoritePasswords.toPasswordItems(addUsernames).sortedBy { it.title.lowercase() })
        addAll(favoritePlainTexts.toPlainTextItems().sortedBy { it.title.lowercase() })
      }
      if (nonFavoritePasswords.isNotEmpty()) {
        add(Title(R.string.text_passwords))
        addAll(nonFavoritePasswords.toPasswordItems(addUsernames).sortedBy { it.title.lowercase() })
      }
      if (nonFavoritePlainTexts.isNotEmpty()) {
        add(Title(R.string.text_plain_texts))
        addAll(nonFavoritePlainTexts.toPlainTextItems().sortedBy { it.title.lowercase() })
      }
    }
  }
  
  private fun List<Entry>.toPlainTextItems(): List<PlainTextItem> {
    var counter = 0
    return map {
      val title = it.fields.title?.content
      val hasActualTitle: Boolean
      val resultTitle = if (title?.isNotBlank() == true) {
        hasActualTitle = true
        title
      } else {
        hasActualTitle = false
        "note$counter"
      }
      counter++
      PlainTextItem(id = it.uuid.toString(), title = resultTitle, hasActualTitle = hasActualTitle)
    }
  }
  
  private fun List<Entry>.toPasswordItems(addUsernames: Boolean): List<PasswordItem> {
    var counter = 0
    return map {
      val title = it.fields.title?.content
      val username = it.fields.userName?.content.orEmpty()
      val hasActualTitle: Boolean
      val resultTitle = if (title?.isNotBlank() == true) {
        hasActualTitle = true
        title
      } else {
        hasActualTitle = false
        "password$counter"
      }
      counter++
      PasswordItem(
        id = it.uuid.toString(),
        title = resultTitle,
        username = if (addUsernames) username else "",
        hasActualTitle = hasActualTitle
      )
    }
  }
}
