package com.arsvechkarev.vault.tests

import com.arsvechkarev.vault.data.BasicDatabase
import com.arsvechkarev.vault.data.BasicDatabase2
import com.arsvechkarev.vault.features.common.data.files.StorageBackupDatabaseFileSaver
import com.arsvechkarev.vault.features.common.data.files.StorageBackupPreferences
import com.arsvechkarev.vault.features.common.domain.StorageBackupInteractor
import com.arsvechkarev.vault.stubs.StubDatabaseChangesJournal
import com.arsvechkarev.vault.stubs.StubDatabaseFileSaver
import com.arsvechkarev.vault.stubs.StubDateTimeFormatter
import com.arsvechkarev.vault.stubs.StubPreferences
import com.arsvechkarev.vault.stubs.StubStorageBackupExternalFileSaver
import com.arsvechkarev.vault.stubs.StubTimestampProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StorageBackupTest {
  
  private val unconfinedTestDispatcher = UnconfinedTestDispatcher()
  
  private val stubUri = "content://com.android.externalstorage.documents/tree/primary%3ABackups"
  
  private val externalFileSaver = StubStorageBackupExternalFileSaver()
  private val storageBackupPreferences = StorageBackupPreferences(StubPreferences())
  private val databaseChangesJournal = StubDatabaseChangesJournal()
  private val timestampProvider = StubTimestampProvider()
  private val dateTimeFormatter = StubDateTimeFormatter()
  private val databaseFileSaver = StubDatabaseFileSaver()
  
  private val storageBackupInteractor = StorageBackupInteractor(
    storageBackupExternalFileSaver = externalFileSaver,
    preferences = storageBackupPreferences,
    journal = databaseChangesJournal,
    timestampProvider = timestampProvider,
    dateTimeFormatter = dateTimeFormatter,
    5L,
    3,
    3
  )
  
  private val storageBackupDatabaseFileSaver = StorageBackupDatabaseFileSaver(
    databaseFileSaver = databaseFileSaver,
    databaseChangesJournal = databaseChangesJournal,
    storageBackupInteractor = storageBackupInteractor,
    TestScope(unconfinedTestDispatcher),
  )
  
  @Test
  fun `Backup is saved when forced`() = runTest(unconfinedTestDispatcher) {
    storageBackupPreferences.enableBackup(stubUri)
    storageBackupInteractor.forceBackup(BasicDatabase)
    
    assertEquals(1, externalFileSaver.backups.size)
    assertEquals(BasicDatabase, externalFileSaver.backups.first().database)
  }
  
  @Test
  fun `Backup is not performed if not enabled`() = runTest(unconfinedTestDispatcher) {
    storageBackupDatabaseFileSaver.save(BasicDatabase)
    storageBackupDatabaseFileSaver.save(BasicDatabase)
    storageBackupDatabaseFileSaver.save(BasicDatabase)
    storageBackupDatabaseFileSaver.save(BasicDatabase)
    
    assertEquals(0, externalFileSaver.backups.size)
  }
  
  @Test
  fun `Backup is performed when made some database changes`() = runTest(unconfinedTestDispatcher) {
    storageBackupPreferences.enableBackup(stubUri)
    
    storageBackupDatabaseFileSaver.save(BasicDatabase)
    
    assertEquals(0, externalFileSaver.backups.size)
    
    storageBackupDatabaseFileSaver.save(BasicDatabase)
    storageBackupDatabaseFileSaver.save(BasicDatabase2)
    
    assertEquals(1, externalFileSaver.backups.size)
    assertEquals(BasicDatabase2, externalFileSaver.backups.first().database)
  }
  
  @Test
  fun `Backup is performed when passed enough time`() = runTest(unconfinedTestDispatcher) {
    storageBackupPreferences.enableBackup(stubUri)
    storageBackupDatabaseFileSaver.save(BasicDatabase)
    
    timestampProvider.now = 10
    
    storageBackupDatabaseFileSaver.save(BasicDatabase2)
    
    assertEquals(1, externalFileSaver.backups.size)
    assertEquals(BasicDatabase2, externalFileSaver.backups.first().database)
  }
  
  @Test
  fun `Extra backups removed when limit is passed`() = runTest(unconfinedTestDispatcher) {
    storageBackupPreferences.enableBackup(stubUri)
    
    repeat(10) {
      externalFileSaver.saveDatabase("dir", "test$it", BasicDatabase)
    }
    
    timestampProvider.now = 10
    
    storageBackupDatabaseFileSaver.save(BasicDatabase2)
    
    assertEquals(3, externalFileSaver.backups.size)
    assertEquals(BasicDatabase, externalFileSaver.backups.first().database)
    assertEquals(BasicDatabase, externalFileSaver.backups[1].database)
    assertEquals(BasicDatabase2, externalFileSaver.backups.last().database)
  }
}
