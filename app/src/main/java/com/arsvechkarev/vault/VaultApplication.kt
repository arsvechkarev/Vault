package com.arsvechkarev.vault

import android.app.Application
import com.arsvechkarev.vault.core.Singletons
import com.arsvechkarev.vault.viewbuilding.Fonts
import com.arsvechkarev.vault.viewdsl.ContextHolder

class VaultApplication : Application() {
  
  override fun onCreate() {
    super.onCreate()
    ContextHolder.init(applicationContext)
    Fonts.init(applicationContext)
    Singletons.init(applicationContext)
  }
}