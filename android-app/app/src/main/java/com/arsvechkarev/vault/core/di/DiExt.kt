package com.arsvechkarev.vault.core.di

import navigation.BaseFragmentScreen

val BaseFragmentScreen.appComponent: AppComponent
  get() {
    return (requireActivity() as AppComponentProvider).appComponent
  }
