package com.arsvechkarev.vault

import android.app.Application
import com.arsvechkarev.vault.core.Singletons
import com.arsvechkarev.vault.viewbuilding.Fonts
import com.arsvechkarev.vault.viewdsl.ContextHolder
import timber.log.Timber

class VaultApplication : Application() {
  
  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
    ContextHolder.init(applicationContext)
    Fonts.init(applicationContext)
    Singletons.init(applicationContext)
  }
}