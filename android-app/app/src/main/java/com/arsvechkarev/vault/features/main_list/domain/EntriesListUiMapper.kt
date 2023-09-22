package com.arsvechkarev.vault.features.main_list.domain

import app.keemobile.kotpass.database.KeePassDatabase
import app.keemobile.kotpass.database.getEntries
import app.keemobile.kotpass.models.Entry
import buisnesslogic.isDefinitePlainText
import buisnesslogic.isProbablePlainText
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.common.model.PasswordItem
import com.arsvechkarev.vault.features.common.model.PlainTextItem
import com.arsvechkarev.vault.features.common.model.Title
import com.arsvechkarev.vault.recycler.DifferentiableItem

class EntriesListUiMapper {
  
  fun mapEntries(database: KeePassDatabase): List<DifferentiableItem> {
    val allEntries = database.getEntries { true }.flatMap { pair -> pair.second }
    val plainTexts = allEntries.filter { it.isDefinitePlainText || it.isProbablePlainText }
    val passwords = allEntries - plainTexts.toSet()
    return buildList {
      if (passwords.isNotEmpty()) {
        add(Title(R.string.text_passwords))
      }
      addAll(passwords.toPasswordItems().sortedBy { it.title.lowercase() })
      if (plainTexts.isNotEmpty()) {
        add(Title(R.string.text_plain_texts))
      }
      addAll(plainTexts.toPlainTextItems().sortedBy { it.title.lowercase() })
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
  
  private fun List<Entry>.toPasswordItems(): List<PasswordItem> {
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
        username = username,
        hasActualTitle = hasActualTitle
      )
    }
  }
}