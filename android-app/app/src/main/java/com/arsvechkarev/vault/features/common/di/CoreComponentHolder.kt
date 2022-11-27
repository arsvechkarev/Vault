package com.arsvechkarev.vault.features.common.di

import android.app.Application

object CoreComponentHolder {
  
  private var _coreComponent: CoreComponent? = null
  
  val coreComponent: CoreComponent
    get() = checkNotNull(_coreComponent) { "Component was not created" }
  
  fun createCoreComponent(application: Application) {
    assert(_coreComponent == null) { "Component was already created" }
    _coreComponent = CoreComponent.create(application)
  }
}
