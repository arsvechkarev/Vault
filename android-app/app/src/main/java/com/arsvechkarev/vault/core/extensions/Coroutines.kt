package com.arsvechkarev.vault.core.extensions

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

val View.coroutineScope: CoroutineScope
  get() {
    return CoroutineScope(Dispatchers.Main)
  }