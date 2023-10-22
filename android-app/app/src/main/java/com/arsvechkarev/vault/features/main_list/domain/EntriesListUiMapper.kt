package com.arsvechkarev.vault.features.main_list.domain

import app.keemobile.kotpass.database.KeePassDatabase
import app.keemobile.kotpass.database.getEntries
import app.keemobile.kotpass.models.Entry
import com.arsvechkarev.vault.features.common.model.PasswordItem
import com.arsvechkarev.vault.features.common.model.PlainTextItem
import com.arsvechkarev.vault.features.common.model.Title
import com.arsvechkarev.vault.features.common.model.Title.Type
import com.arsvechkarev.vault.recycler.DifferentiableItem
import domain.isDefinitePlainText
import domain.isFavorite
import domain.isProbablePlainText

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
        add(Title(Type.FAVORITES))
        addAll(favoritePasswords.toPasswordItems(addUsernames).sortedBy { it.title.lowercase() })
        addAll(favoritePlainTexts.toPlainTextItems().sortedBy { it.title.lowercase() })
      }
      if (nonFavoritePasswords.isNotEmpty()) {
        add(Title(Type.PASSWORDS))
        addAll(nonFavoritePasswords.toPasswordItems(addUsernames).sortedBy { it.title.lowercase() })
      }
      if (nonFavoritePlainTexts.isNotEmpty()) {
        add(Title(Type.PLAIN_TEXTS))
        addAll(nonFavoritePlainTexts.toPlainTextItems().sortedBy { it.title.lowercase() })
      }
    }
  }
  
  private fun List<Entry>.toPasswordItems(addUsernames: Boolean): List<PasswordItem> {
    return mapIndexed { i, entry ->
      val counter = i + 1
      val title = entry.fields.title?.content
      val username = entry.fields.userName?.content.orEmpty()
      val hasActualTitle: Boolean
      val resultTitle = if (title?.isNotBlank() == true) {
        hasActualTitle = true
        title
      } else {
        hasActualTitle = false
        "password$counter"
      }
      PasswordItem(
        id = entry.uuid.toString(),
        title = resultTitle,
        username = if (addUsernames) username else "",
        hasActualTitle = hasActualTitle
      )
    }
  }
  
  private fun List<Entry>.toPlainTextItems(): List<PlainTextItem> {
    return mapIndexed { i, entry ->
      val counter = i + 1
      val title = entry.fields.title?.content
      val hasActualTitle: Boolean
      val resultTitle = if (title?.isNotBlank() == true) {
        hasActualTitle = true
        title
      } else {
        hasActualTitle = false
        "note$counter"
      }
      PlainTextItem(
        id = entry.uuid.toString(),
        title = resultTitle,
        hasActualTitle = hasActualTitle
      )
    }
  }
}
