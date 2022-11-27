package com.arsvechkarev.vault.features.common.di

import navigation.BaseFragmentScreen

val BaseFragmentScreen.appComponent: AppComponent
  get() {
    return (requireActivity() as AppComponentProvider).appComponent
  }
