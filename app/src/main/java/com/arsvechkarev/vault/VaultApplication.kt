package com.arsvechkarev.vault

import android.app.Application
import com.arsvechkarev.vault.core.viewbuilding.Colors
import com.arsvechkarev.vault.core.viewbuilding.Fonts
import com.arsvechkarev.vault.core.viewdsl.ContextHolder

class VaultApplication : Application() {
  
  override fun onCreate() {
    super.onCreate()
    ContextHolder.init(applicationContext)
    Fonts.init(applicationContext)
  }
}