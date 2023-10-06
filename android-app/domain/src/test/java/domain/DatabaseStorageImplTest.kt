package domain

import domain.model.PasswordEntry
import domain.model.PlainTextEntry
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DatabaseStorageImplTest {
  
  private val testFileSaver = TestDatabaseFileSaver()
  
  private val testPassword = "pAsSw0rd"
  
  @Before
  fun setup() = runBlocking {
  }
  
  @After
  fun tearDown() = runBlocking {
//    testFileSaver.delete()
  }
  
  @Test
  fun `Getting entries for the first time`() = runBlocking {
    val entries = storage.getDatabase(testPassword)
    assertTrue(entries.passwords.isEmpty())
    assertTrue(entries.creditCards.isEmpty())
    assertTrue(entries.plainTexts.isEmpty())
  }
  
  @Test
  fun `Saving entries`() = runBlocking {
    val passwordEntry = PasswordEntry(
      id = ",cml29",
      title = "google",
      username = "pro",
      password = Password.create("po39,x//2"),
      url = "",
      notes = ""
    )
    val plainTextEntry = PlainTextEntry("lkdvj", "title", "my secret data")
    
    storage.saveEntry(testPassword, passwordEntry)
    storage.saveEntry(testPassword, plainTextEntry)
    
    val entries = storage.getDatabase(testPassword)
    assertTrue(entries.passwords.size == 1)
    assertTrue(entries.creditCards.size == 1)
    assertTrue(entries.plainTexts.size == 1)
    assertTrue(entries.passwords.contains(passwordEntry))
    assertTrue(entries.plainTexts.contains(plainTextEntry))
  }
  
  @Test
  fun `Updating entries`() = runBlocking {
    val plainTextEntry = PlainTextEntry(
      id = "09f42",
      title = "the title",
      text = "lalala"
    )
    val passwordEntry = PasswordEntry(
      id = "sdofgi",
      title = "netflix",
      username = "lol",
      password = Password.create("wasp"),
      url = "",
      notes = ""
    )
    val passwordEntryUpdated = PasswordEntry(
      id = "sdofgi",
      title = "netflix",
      username = "newUser",
      password = Password.create("wasp"),
      url = "",
      notes = ""
    )
    
    storage.saveEntry(testPassword, plainTextEntry)
    storage.saveEntry(testPassword, passwordEntry)
    storage.updateEntry(testPassword, passwordEntryUpdated)
    
    val entries = storage.getDatabase(testPassword)
    assertTrue(entries.plainTexts.size == 1)
    assertTrue(entries.passwords.size == 1)
    assertTrue(entries.creditCards.isEmpty())
    assertTrue(entries.plainTexts.contains(plainTextEntry))
    assertTrue(entries.passwords.contains(passwordEntryUpdated))
  }
  
  @Test
  fun `Deleting services`() = runBlocking {
    val firstPasswordEntry = PasswordEntry(
      id = "23490uwriojefk",
      title = "google",
      username = "pro",
      password = Password.create("po39,x//2"),
      url = "",
      notes = "",
    )
    val secondPasswordEntry = PasswordEntry(
      id = ",;pl9oki87ju6hy5gt",
      title = "netflix",
      username = "lol",
      password = Password.create("wasp"),
      url = "",
      notes = ""
    )
    
    storage.saveEntry(testPassword, firstPasswordEntry)
    storage.saveEntry(testPassword, secondPasswordEntry)
    storage.deleteEntry(testPassword, secondPasswordEntry)
    
    val entries = storage.getDatabase(testPassword)
    assertTrue(entries.passwords.size == 1)
    assertTrue(entries.passwords.contains(firstPasswordEntry))
  }
}
