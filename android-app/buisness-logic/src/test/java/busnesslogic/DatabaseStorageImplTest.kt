package busnesslogic

import buisnesslogic.AesSivTinkCryptography
import buisnesslogic.KeePassStorageImpl
import buisnesslogic.model.CreditCardEntry
import buisnesslogic.model.PasswordEntry
import buisnesslogic.model.PlainTextEntry
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DatabaseStorageImplTest {
  
  private val testFileSaver = TestDatabaseFileSaver()
  
  private val testPassword = "pAsSw0rd"
  
  private val storage = KeePassStorageImpl(
    AesSivTinkCryptography,
    testFileSaver,
    Gson(),
  )
  
  @Before
  fun setup() = runBlocking {
    val encryptedText = AesSivTinkCryptography.encryptData(testPassword, "")
    testFileSaver.save(encryptedText)
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
      websiteName = "google",
      login = "pro",
      password = "po39,x//2",
      notes = ""
    )
    val creditCardEntry = CreditCardEntry(
      id = "90lk290",
      cardNumber = "456978921234567",
      expirationDate = "10/26",
      cardholderName = "VASYA SOMETHING",
      cvcCode = "999",
      pinCode = "1234",
      notes = ""
    )
    val plainTextEntry = PlainTextEntry("lkdvj", "title", "my secret data")
    
    storage.saveEntry(testPassword, passwordEntry)
    storage.saveEntry(testPassword, creditCardEntry)
    storage.saveEntry(testPassword, plainTextEntry)
    
    val entries = storage.getDatabase(testPassword)
    assertTrue(entries.passwords.size == 1)
    assertTrue(entries.creditCards.size == 1)
    assertTrue(entries.plainTexts.size == 1)
    assertTrue(entries.passwords.contains(passwordEntry))
    assertTrue(entries.creditCards.contains(creditCardEntry))
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
      websiteName = "netflix",
      login = "lol",
      password = "wasp",
      notes = ""
    )
    val passwordEntryUpdated = PasswordEntry(
      id = "sdofgi",
      websiteName = "netflix",
      login = "newUser",
      password = "wasp",
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
      websiteName = "google",
      login = "pro",
      password = "po39,x//2",
      notes = ""
    )
    val secondPasswordEntry = PasswordEntry(
      id = ",;pl9oki87ju6hy5gt",
      websiteName = "netflix",
      login = "lol",
      password = "wasp",
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
