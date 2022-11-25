package com.arsvechkarev.vault.core.mvi.ext

import androidx.lifecycle.ViewModelStoreOwner
import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import kotlin.properties.ReadOnlyProperty

fun <S : TeaStore<*, *, *>> ViewModelStoreOwner.viewModelStore(
  key: String? = null,
  factory: () -> S
): ReadOnlyProperty<ViewModelStoreOwner, S> {
  return ViewModelProperty({ this }, key, factory)
}

