package busnesslogic

import buisnesslogic.CryptographyImpl
import buisnesslogic.GsonJsonConverter
import buisnesslogic.ServicesStorageImpl
import buisnesslogic.base64.JavaBase64Coder
import buisnesslogic.model.ServiceEntity
import buisnesslogic.random.SeedRandomGeneratorImpl
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ServicesStorageImplTest {
  
  private val testFileSaver = TestFileSaver()
  private val testPassword = "pAsSw0rd"
  
  private val cryptography = CryptographyImpl(JavaBase64Coder, SeedRandomGeneratorImpl)
  private val storage = ServicesStorageImpl(cryptography, testFileSaver, GsonJsonConverter)
  
  @Before
  fun setUp() {
    val encryptedText = cryptography.encryptForTheFirstTime(testPassword, "")
    testFileSaver.saveTextToFile(encryptedText)
  }
  
  @After
  fun tearDown() {
    testFileSaver.saveTextToFile("")
  }
  
  @Test
  fun `Getting services list for the first time`() = runBlocking {
    val servicesList = storage.getServices(testPassword)
    assertTrue(servicesList.isEmpty())
  }
  
  @Test
  fun `Saving new services`() = runBlocking {
    val service1 = ServiceEntity("id", "google", "pro", "", "po39,x//2")
    val service2 = ServiceEntity("id2", "netflix", "lol", "", "wsald0k")
    
    storage.saveService(testPassword, service1)
    storage.saveService(testPassword, service2)
    val services = storage.getServices(testPassword)
    
    assertTrue(services.size == 2)
    assertTrue(services.contains(service1))
    assertTrue(services.contains(service2))
  }
  
  @Test
  fun `Updating services`() = runBlocking {
    val service1 = ServiceEntity("id", "google", "pro", "", "po39,x//2")
    val service2 = ServiceEntity("id2", "netflix", "lol", "", "wasp")
    val service2Updated = ServiceEntity("id2", "netflix", "newUser", "", "wasp")
    
    storage.saveService(testPassword, service1)
    storage.saveService(testPassword, service2)
    storage.updateService(testPassword, service2Updated)
    val services = storage.getServices(testPassword)
    
    assertTrue(services.size == 2)
    assertTrue(services.contains(service1))
    assertTrue(services.contains(service2Updated))
  }
  
  @Test
  fun `Deleting services`() = runBlocking {
    val service1 = ServiceEntity("id", "google", "pro", "", "po39,x//2")
    val service2 = ServiceEntity("id2", "netflix", "lol", "", "wasp")
    
    storage.saveService(testPassword, service1)
    storage.saveService(testPassword, service2)
    storage.deleteService(testPassword, service2)
    val services = storage.getServices(testPassword)
    
    assertTrue(services.size == 1)
    assertTrue(services.contains(service1))
  }
}