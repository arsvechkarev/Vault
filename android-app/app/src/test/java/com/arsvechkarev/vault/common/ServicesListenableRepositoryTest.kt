package com.arsvechkarev.vault.common

class ServicesListenableRepositoryTest {
  
  //  private val testFileSaver = TestFileSaver()
  //  private val testPassword = "pAsSw0rd"
  //  private val cryptography = CryptographyImpl(JavaBase64Coder, SeedRandomGeneratorImpl)
  //  private val storage = ServicesStorageImpl(cryptography, testFileSaver, GsonJsonConverter)
  //  private val repository = ServicesListenableRepository(storage)
  //
  //  @Before
  //  fun setUp() {
  //    val encryptedText = cryptography.encryptForTheFirstTime(testPassword, "")
  //    testFileSaver.saveData(encryptedText)
  //  }
  //
  //  @After
  //  fun tearDown() {
  //    testFileSaver.saveData("")
  //  }
  //
  //  @Test
  //  fun `Getting services list for the first time`() = runBlocking {
  //    val servicesList = repository.getServices(testPassword)
  //    assertTrue(servicesList.isEmpty())
  //  }
  //
  //  @Test
  //  fun `Testing whether services are sorted`() = runBlocking {
  //    val service1 = ServiceModel("id", "google", "pro", "", "po39,x//2")
  //    val service2 = ServiceModel("id2", "A_netflix", "lol", "", "prow")
  //    val service3 = ServiceModel("id0", "zxzz", "kek", "", ".oo03")
  //
  //    repository.saveService(testPassword, service1)
  //    repository.saveService(testPassword, service2)
  //    repository.saveService(testPassword, service3)
  //
  //    val servicesList = repository.getServices(testPassword)
  //    assertTrue(servicesList.indexOf(service2) == 0)
  //    assertTrue(servicesList.indexOf(service1) == 1)
  //    assertTrue(servicesList.indexOf(service3) == 2)
  //  }
  //
  //  @Test
  //  fun `Updating services`() = runBlocking {
  //    val service1 = ServiceModel("id", "google", "pro", "", "po39,x//2")
  //    val service2 = ServiceModel("id2", "netflix", "lol", "", "wasp")
  //    val service2Updated = ServiceModel("id2", "netflix", "newUser", "", "wasp")
  //
  //    repository.saveService(testPassword, service1)
  //    repository.saveService(testPassword, service2)
  //    repository.updateService(testPassword, service2Updated)
  //    val services = repository.getServices(testPassword)
  //
  //    assertTrue(services.size == 2)
  //    assertTrue(services.contains(service1))
  //    assertTrue(services.contains(service2Updated))
  //  }
  //
  //  @Test
  //  fun `Deleting services`() = runBlocking {
  //    val service1 = ServiceModel("id", "google", "pro", "", "po39,x//2")
  //    val service2 = ServiceModel("id2", "netflix", "lol", "", "wasp")
  //
  //    repository.saveService(testPassword, service1)
  //    repository.saveService(testPassword, service2)
  //    repository.deleteService(testPassword, service2, notifySubscribers = false)
  //    val services = repository.getServices(testPassword)
  //
  //    assertTrue(services.size == 1)
  //    assertTrue(services.contains(service1))
  //  }
}
