package com.arsvechkarev.vault.core.di

import navigation.BaseFragmentScreen
import navigation.BaseScreen

val BaseScreen.appComponent: AppComponent
  get() {
    return (viewNonNull.context as AppComponentProvider).appComponent
  }

val BaseFragmentScreen.appComponent: AppComponent
  get() {
    return (requireActivity() as AppComponentProvider).appComponent
  }
