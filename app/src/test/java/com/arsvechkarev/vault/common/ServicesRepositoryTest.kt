package com.arsvechkarev.vault.common

import buisnesslogic.Cryptography
import buisnesslogic.GsonJsonConverter
import buisnesslogic.ServicesStorageImpl
import buisnesslogic.base64.JavaBase64Coder
import buisnesslogic.random.SeedRandomGeneratorImpl
import com.arsvechkarev.vault.core.model.ServiceModel
import com.arsvechkarev.vault.features.common.ServicesRepository
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ServicesRepositoryTest {
  
  private val testFileSaver = TestFileSaver()
  private val testPassword = "pAsSw0rd"
  private val cryptography = Cryptography(JavaBase64Coder, SeedRandomGeneratorImpl)
  private val storage = ServicesStorageImpl(cryptography, testFileSaver, GsonJsonConverter)
  private val repository = ServicesRepository(storage, FakeThreader)
  
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
  fun `Getting services list for the first time`() {
    val servicesList = repository.getServices(testPassword)
    assertTrue(servicesList.isEmpty())
  }
  
  @Test
  fun `Testing whether services are sorted`() {
    val service1 = ServiceModel("id", "google", "pro", "", "po39,x//2")
    val service2 = ServiceModel("id2", "A_netflix", "lol", "", "prow")
    val service3 = ServiceModel("id0", "zxzz", "kek", "", ".oo03")
  
    repository.saveService(testPassword, service1)
    repository.saveService(testPassword, service2)
    repository.saveService(testPassword, service3)
  
    val servicesList = repository.getServices(testPassword)
    assertTrue(servicesList.indexOf(service2) == 0)
    assertTrue(servicesList.indexOf(service1) == 1)
    assertTrue(servicesList.indexOf(service3) == 2)
  }
  
  @Test
  fun `Updating services`() {
    val service1 = ServiceModel("id", "google", "pro", "", "po39,x//2")
    val service2 = ServiceModel("id2", "netflix", "lol", "", "wasp")
    val service2Updated = ServiceModel("id2", "netflix", "newUser", "", "wasp")
  
    repository.saveService(testPassword, service1)
    repository.saveService(testPassword, service2)
    repository.updateService(testPassword, service2Updated)
    val services = repository.getServices(testPassword)
  
    assertTrue(services.size == 2)
    assertTrue(services.contains(service1))
    assertTrue(services.contains(service2Updated))
  }
  
  @Test
  fun `Deleting services`() {
    val service1 = ServiceModel("id", "google", "pro", "", "po39,x//2")
    val service2 = ServiceModel("id2", "netflix", "lol", "", "wasp")
  
    repository.saveService(testPassword, service1)
    repository.saveService(testPassword, service2)
    repository.deleteService(testPassword, service2, notifyListeners = false)
    val services = repository.getServices(testPassword)
  
    assertTrue(services.size == 1)
    assertTrue(services.contains(service1))
  }
  
  @Test
  fun `Testing change listener`() {
    var list: List<ServiceModel> = ArrayList()
    var isListenerCalled = false
    val listener: ((List<ServiceModel>) -> Unit) = {
      isListenerCalled = true
      list = it
    }
  
    val service = ServiceModel("id", "google", "pro", "", "po39,x//2")
    repository.addChangeListener(listener)
    repository.saveService(testPassword, service)
    
    assertTrue(isListenerCalled)
    assertTrue(list.size == 1)
    assertTrue(list.contains(service))
  }
  
  @Test
  fun `Testing removing of change listener`() {
    var isListenerCalled = false
    val listener: ((List<ServiceModel>) -> Unit) = {
      isListenerCalled = !isListenerCalled
    }
  
    val service = ServiceModel("id", "google", "pro", "", "po39,x//2")
    repository.addChangeListener(listener)
    repository.saveService(testPassword, service)
    
    assertTrue(isListenerCalled)
    
    repository.removeChangeListener(listener)
    repository.deleteService(testPassword, service, notifyListeners = true)
    
    assertTrue(isListenerCalled)
  }
}