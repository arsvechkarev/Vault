package com.arsvechkarev.vault.tests

import com.arsvechkarev.vault.data.BasicDatabase
import com.arsvechkarev.vault.data.NonStandardDatabase
import com.arsvechkarev.vault.features.common.model.PasswordItem
import com.arsvechkarev.vault.features.common.model.NoteItem
import com.arsvechkarev.vault.features.common.model.Title
import com.arsvechkarev.vault.features.common.model.Title.Type
import com.arsvechkarev.vault.features.main_list.domain.EntriesListUiMapper
import com.arsvechkarev.vault.recycler.DifferentiableItem
import org.junit.Assert.assertEquals
import org.junit.Test

class EntriesListUiMapperTest {
  
  private val mapper = EntriesListUiMapper()
  
  @Test
  fun `Test data with usernames`() {
    assertEquals(
      listOf(
        Title(
          type = Type.PASSWORDS
        ),
        PasswordItem(
          id = "c13dde14-cb66-4f50-95c0-2d636bf05bcd",
          title = "google",
          username = "me@gmail.com",
          hasActualTitle = true
        ),
        Title(
          type = Type.NOTES
        ),
        NoteItem(
          id = "3d2f85f1-490a-4290-8d66-7de9f9769858",
          title = "test",
          hasActualTitle = true,
        )
      ),
      mapper.mapItems(BasicDatabase, showUsernames = true, filterQuery = "")
    )
  }
  
  
  @Test
  fun `Test data without usernames`() {
    assertEquals(
      listOf(
        Title(
          type = Type.PASSWORDS
        ),
        PasswordItem(
          id = "c13dde14-cb66-4f50-95c0-2d636bf05bcd",
          title = "google",
          username = "",
          hasActualTitle = true
        ),
        Title(
          type = Type.NOTES
        ),
        NoteItem(
          id = "3d2f85f1-490a-4290-8d66-7de9f9769858",
          title = "test",
          hasActualTitle = true,
        )
      ),
      mapper.mapItems(BasicDatabase, showUsernames = false, filterQuery = "")
    )
  }
  
  @Test
  fun `Test mapping non-standard data`() {
    assertEquals(
      listOf(
        Title(
          type = Type.PASSWORDS
        ),
        PasswordItem(
          id = "c13dde14-cb66-4f50-95c0-2d636bf05bcd",
          title = "password1",
          username = "",
          hasActualTitle = false
        ),
        PasswordItem(
          id = "fe674044-8e7b-4ba2-b316-20bf841d3bcd",
          title = "password2",
          username = "lalala",
          hasActualTitle = false
        ),
        Title(
          type = Type.NOTES
        ),
        NoteItem(
          id = "3d2f85f1-490a-4290-8d66-7de9f9769858",
          title = "note1",
          hasActualTitle = false,
        ),
        NoteItem(
          id = "86916deb-74b4-4b1c-9f52-517eaad3aad6",
          title = "note2",
          hasActualTitle = false,
        )
      ),
      mapper.mapItems(NonStandardDatabase, showUsernames = true, filterQuery = "")
    )
  }
  
  @Test
  fun `Test basic search`() {
    assertEquals(
      listOf(
        Title(
          type = Type.PASSWORDS
        ),
        PasswordItem(
          id = "c13dde14-cb66-4f50-95c0-2d636bf05bcd",
          title = "password1",
          username = "",
          hasActualTitle = false
        ),
        PasswordItem(
          id = "fe674044-8e7b-4ba2-b316-20bf841d3bcd",
          title = "password2",
          username = "lalala",
          hasActualTitle = false
        ),
      ),
      mapper.mapItems(NonStandardDatabase, showUsernames = true, filterQuery = "password")
    )
  }
  
  @Test
  fun `Test search with usernames`() {
    assertEquals(
      listOf(
        Title(
          type = Type.PASSWORDS
        ),
        PasswordItem(
          id = "c13dde14-cb66-4f50-95c0-2d636bf05bcd",
          title = "google",
          username = "me@gmail.com",
          hasActualTitle = true
        ),
      ),
      mapper.mapItems(BasicDatabase, showUsernames = true, filterQuery = "gma")
    )
  }
  
  @Test
  fun `Test empty search`() {
    assertEquals(
      emptyList<DifferentiableItem>(),
      mapper.mapItems(BasicDatabase, showUsernames = true, filterQuery = "abc")
    )
  }
}
