package com.arsvechkarev.vault.core.di

import navigation.BaseScreen

val BaseScreen.appComponent: AppComponent
  get() {
    return (viewNonNull.context as AppComponentProvider).appComponent
  }