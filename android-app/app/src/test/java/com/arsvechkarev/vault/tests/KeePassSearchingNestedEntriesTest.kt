package com.arsvechkarev.vault.tests

import com.arsvechkarev.vault.data.DatabaseWithGroups
import com.arsvechkarev.vault.features.common.model.NoteItem
import com.arsvechkarev.vault.features.common.model.PasswordItem
import com.arsvechkarev.vault.features.main_list.domain.EntriesListUiMapper
import domain.IdGeneratorImpl
import domain.RealInstantProvider
import domain.UniqueIdProvideImpl
import domain.interactors.KeePassNoteEntryInteractor
import domain.interactors.KeePassPasswordEntryInteractor
import org.junit.Assert.assertEquals
import org.junit.Test

class KeePassSearchingNestedEntriesTest {
  
  private val passwordModelInteractor = KeePassPasswordEntryInteractor(
    UniqueIdProvideImpl(IdGeneratorImpl), RealInstantProvider
  )
  
  private val noteEntryInteractor = KeePassNoteEntryInteractor(
    UniqueIdProvideImpl(IdGeneratorImpl), RealInstantProvider
  )
  
  private val entriesListUiMapper = EntriesListUiMapper()
  
  @Test
  fun testSearchingNestedPassword() {
    val items = entriesListUiMapper.mapItems(DatabaseWithGroups, false, "")
    val id = items.find { (it as? PasswordItem)?.title == "test_inner" }?.id
        ?: error("Entry 'test_inner' not found")
    val entry = passwordModelInteractor.getPasswordEntry(DatabaseWithGroups, id)
    assertEquals(id, entry.id)
    assertEquals("test_inner", entry.title)
  }
  
  @Test
  fun testSearchingNestedNote() {
    val items = entriesListUiMapper.mapItems(DatabaseWithGroups, false, "")
    val id = items.find { (it as? NoteItem)?.title == "Note" }?.id
        ?: error("Entry 'Note' not found")
    val entry = noteEntryInteractor.getNoteEntry(DatabaseWithGroups, id)
    assertEquals(id, entry.id)
    assertEquals("Note", entry.title)
  }
}
