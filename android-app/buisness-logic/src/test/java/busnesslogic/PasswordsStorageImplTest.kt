package busnesslogic

import buisnesslogic.AesSivTinkCryptography
import buisnesslogic.GsonJsonConverter
import buisnesslogic.PasswordsStorageImpl
import buisnesslogic.model.PasswordInfo
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PasswordsStorageImplTest {
  
  private val testFileSaver = TestFileSaver()
  private val testPassword = "pAsSw0rd"
  
  private val storage = PasswordsStorageImpl(AesSivTinkCryptography, testFileSaver,
    GsonJsonConverter)
  
  @Before
  fun setUp() = runBlocking {
    val encryptedText = AesSivTinkCryptography.encryptData(testPassword, "")
    testFileSaver.saveData(encryptedText)
  }
  
  @After
  fun tearDown() = runBlocking {
    testFileSaver.delete()
  }
  
  @Test
  fun `Getting services list for the first time`() = runBlocking {
    val servicesList = storage.getPasswords(testPassword)
    assertTrue(servicesList.isEmpty())
  }
  
  @Test
  fun `Saving new services`() = runBlocking {
    val service1 = PasswordInfo("id", "google", "pro", "", "po39,x//2")
    val service2 = PasswordInfo("id2", "netflix", "lol", "", "wsald0k")
    
    storage.savePassword(testPassword, service1)
    storage.savePassword(testPassword, service2)
    val services = storage.getPasswords(testPassword)
    
    assertTrue(services.size == 2)
    assertTrue(services.contains(service1))
    assertTrue(services.contains(service2))
  }
  
  @Test
  fun `Updating services`() = runBlocking {
    val service1 = PasswordInfo("id", "google", "pro", "", "po39,x//2")
    val service2 = PasswordInfo("id2", "netflix", "lol", "", "wasp")
    val service2Updated = PasswordInfo("id2", "netflix", "newUser", "", "wasp")
    
    storage.savePassword(testPassword, service1)
    storage.savePassword(testPassword, service2)
    storage.updatePasswordInfo(testPassword, service2Updated)
    val services = storage.getPasswords(testPassword)
    
    assertTrue(services.size == 2)
    assertTrue(services.contains(service1))
    assertTrue(services.contains(service2Updated))
  }
  
  @Test
  fun `Deleting services`() = runBlocking {
    val service1 = PasswordInfo("id", "google", "pro", "", "po39,x//2")
    val service2 = PasswordInfo("id2", "netflix", "lol", "", "wasp")
    
    storage.savePassword(testPassword, service1)
    storage.savePassword(testPassword, service2)
    storage.deletePassword(testPassword, service2)
    val actualServices = storage.getPasswords(testPassword)
    
    assertTrue(actualServices.size == 1)
    assertTrue(actualServices.contains(service1))
  }
}
