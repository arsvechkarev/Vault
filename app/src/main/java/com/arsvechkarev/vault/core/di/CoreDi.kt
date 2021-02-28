package com.arsvechkarev.vault.core.di

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak") // Setting application context, so there should be no leaks
object CoreDi {
  
  private var _coreComponent: CoreComponent? = null
  val coreComponent: CoreComponent get() = _coreComponent!!
  
  fun init(applicationContext: Context) {
    _coreComponent = DaggerCoreComponent.builder()
        .coreModule(CoreModule(applicationContext))
        .build()
  }
}