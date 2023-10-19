package com.arsvechkarev.vault.features.common.di

import android.app.Application

object CoreComponentHolder {
  
  private var _coreComponent: CoreComponent? = null
  
  val coreComponent: CoreComponent
    get() = checkNotNull(_coreComponent) { "Component was not initialized" }
  
  fun initialize(application: Application, factory: ExtraDependenciesFactory) {
    _coreComponent = CoreComponent.create(application, factory)
  }
}
