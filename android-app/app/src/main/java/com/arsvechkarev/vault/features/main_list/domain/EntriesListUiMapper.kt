package com.arsvechkarev.vault.features.main_list.domain

import app.keemobile.kotpass.database.KeePassDatabase
import app.keemobile.kotpass.database.getEntries
import app.keemobile.kotpass.models.Entry
import com.arsvechkarev.vault.features.common.model.NoteItem
import com.arsvechkarev.vault.features.common.model.PasswordItem
import com.arsvechkarev.vault.features.common.model.Title
import com.arsvechkarev.vault.features.common.model.Title.Type
import com.arsvechkarev.vault.recycler.DifferentiableItem
import domain.isDefiniteNote
import domain.isFavorite
import domain.isProbableNote

class EntriesListUiMapper {
  
  fun mapItems(
    database: KeePassDatabase,
    showUsernames: Boolean,
    filterQuery: String
  ): List<DifferentiableItem> {
    val allEntries = database.getEntries { true }.flatMap { pair -> pair.second }
    val notes = allEntries.filter { it.isDefiniteNote || it.isProbableNote }
    val groupedPasswords = (allEntries - notes.toSet()).groupBy { it.isFavorite }
    val favoritePasswords = groupedPasswords[true].orEmpty()
    val nonFavoritePasswords = groupedPasswords[false].orEmpty()
    val groupedNotes = notes.groupBy { it.isFavorite }
    val favoriteNotes = groupedNotes[true].orEmpty()
    val nonFavoriteNotes = groupedNotes[false].orEmpty()
    return buildList {
      val filteredFavoritePasswords = favoritePasswords.toPasswordItems(showUsernames)
          .sortedBy { it.title.lowercase() }.filterByQuery(filterQuery, showUsernames)
      val filteredFavoriteNotes = favoriteNotes.toNoteItems()
          .sortedBy { it.title.lowercase() }.filterByQuery(filterQuery)
      val filteredFavoriteItems = filteredFavoritePasswords + filteredFavoriteNotes
      if (filteredFavoriteItems.isNotEmpty()) {
        add(Title(Type.FAVORITES))
        addAll(filteredFavoriteItems)
      }
      val filteredNonFavoritePasswords = nonFavoritePasswords.toPasswordItems(showUsernames)
          .sortedBy { it.title.lowercase() }.filterByQuery(filterQuery, showUsernames)
      if (filteredNonFavoritePasswords.isNotEmpty()) {
        add(Title(Type.PASSWORDS))
        addAll(filteredNonFavoritePasswords)
      }
      val filteredNonFavoriteNotes = nonFavoriteNotes.toNoteItems()
          .sortedBy { it.title.lowercase() }.filterByQuery(filterQuery)
      if (filteredNonFavoriteNotes.isNotEmpty()) {
        add(Title(Type.NOTES))
        addAll(filteredNonFavoriteNotes)
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
  
  private fun List<Entry>.toNoteItems(): List<NoteItem> {
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
      NoteItem(
        id = entry.uuid.toString(),
        title = resultTitle,
        hasActualTitle = hasActualTitle
      )
    }
  }
  
  private fun List<PasswordItem>.filterByQuery(
    filterQuery: String,
    searchInUsernames: Boolean
  ): List<PasswordItem> {
    if (filterQuery.isEmpty()) {
      return this
    }
    return filter {
      val trimmedQuery = filterQuery.trim()
      it.title.contains(trimmedQuery, ignoreCase = true)
          || (searchInUsernames && it.username.contains(trimmedQuery, ignoreCase = true))
    }
  }
  
  private fun List<NoteItem>.filterByQuery(filterQuery: String): List<NoteItem> {
    if (filterQuery.isEmpty()) {
      return this
    }
    return filter { it.title.contains(filterQuery.trim(), ignoreCase = true) }
  }
}
