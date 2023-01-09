package com.arsvechkarev.vault.features.common.di

import android.app.Application
import com.arsvechkarev.vault.core.DefaultDispatchersFacade
import com.arsvechkarev.vault.features.common.data.ContextExternalFileReader
import com.arsvechkarev.vault.features.common.data.ExternalFileReader
import com.arsvechkarev.vault.features.common.data.PasswordsFileExporter
import com.arsvechkarev.vault.features.common.data.RealPasswordsFileExporter
import com.arsvechkarev.vault.features.common.navigation.ActivityResultSubstitutor
import com.arsvechkarev.vault.features.common.navigation.RealActivityResultSubstitutor

object CoreComponentHolder {
  
  private var _coreComponent: CoreComponent? = null
  
  val coreComponent: CoreComponent
    get() = checkNotNull(_coreComponent) { "Component was not initialized" }
  
  fun initialize(
    application: Application,
    activityResultSubstitutor: ActivityResultSubstitutor = RealActivityResultSubstitutor(),
    passwordsFileExporter: PasswordsFileExporter =
        RealPasswordsFileExporter(application, DefaultDispatchersFacade),
    externalFileReader: ExternalFileReader = ContextExternalFileReader(application),
  ) {
    _coreComponent = CoreComponent.create(
      application,
      activityResultSubstitutor,
      passwordsFileExporter,
      externalFileReader
    )
  }
}
