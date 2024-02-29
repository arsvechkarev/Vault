package com.arsvechkarev.vault.features.common.di

import android.app.Application
import com.arsvechkarev.vault.core.DispatchersFacade
import com.arsvechkarev.vault.features.common.AppConstants
import com.arsvechkarev.vault.features.common.data.files.ContextExternalFileReader
import com.arsvechkarev.vault.features.common.data.files.ContextResolverUriPersistedMaker
import com.arsvechkarev.vault.features.common.data.files.DefaultKeyFileSaver
import com.arsvechkarev.vault.features.common.data.files.ExternalFileReader
import com.arsvechkarev.vault.features.common.data.files.KeyFileSaver
import com.arsvechkarev.vault.features.common.data.files.PasswordsFileExporter
import com.arsvechkarev.vault.features.common.data.files.RealPasswordsFileExporter
import com.arsvechkarev.vault.features.common.data.files.UriPersistedMaker
import com.arsvechkarev.vault.features.common.domain.BackupInterceptor
import com.arsvechkarev.vault.features.common.domain.ImageRequestsRecorder
import com.arsvechkarev.vault.features.common.domain.NoOpBackupInterceptor
import com.arsvechkarev.vault.features.common.domain.NoOpImageRequestsRecorder
import com.arsvechkarev.vault.features.common.navigation.result_contracts.ActivityResultWrapper
import com.arsvechkarev.vault.features.common.navigation.result_contracts.RealActivityResultWrapper
import okhttp3.OkHttpClient

interface ExtraDependencies {
  val okHttpClient: OkHttpClient
  val activityResultWrapper: ActivityResultWrapper
  val passwordsFileExporter: PasswordsFileExporter
  val externalFileReader: ExternalFileReader
  val keyFileSaver: KeyFileSaver
  val imageRequestsRecorder: ImageRequestsRecorder
  val backupInterceptor: BackupInterceptor
  val uriPersistedMaker: UriPersistedMaker
}

class RealExtraDependencies(
  application: Application,
  dispatchers: DispatchersFacade
) : ExtraDependencies {
  
  override val okHttpClient = OkHttpClient()
  
  override val activityResultWrapper = RealActivityResultWrapper()
  
  override val passwordsFileExporter = RealPasswordsFileExporter(application, dispatchers)
  
  override val externalFileReader = ContextExternalFileReader(application)
  
  override val keyFileSaver = DefaultKeyFileSaver(
    AppConstants.DEFAULT_INTERNAL_KEY_FILE_NAME, application, dispatchers
  )
  
  override val imageRequestsRecorder = NoOpImageRequestsRecorder
  
  override val backupInterceptor = NoOpBackupInterceptor
  
  override val uriPersistedMaker = ContextResolverUriPersistedMaker()
}
