package com.arsvechkarev.vault.core.mvi.ext

import androidx.lifecycle.ViewModelStoreOwner
import com.arsvechkarev.vault.core.DefaultDispatchersFacade
import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewModelProperty<out V : TeaStore<*, *, *>>(
  private val viewModelStoreOwnerProvider: () -> ViewModelStoreOwner,
  private val sharedViewModelKey: String?,
  private val storeFactory: () -> V
) : ReadOnlyProperty<Any, V> {
  
  private var value: V? = null
  
  override fun getValue(thisRef: Any, property: KProperty<*>): V {
    return value ?: createValue(property).also { value = it }
  }
  
  private fun createValue(property: KProperty<*>): V {
    val storeOwner = viewModelStoreOwnerProvider()
    val key = sharedViewModelKey ?: keyFromProperty(storeOwner, property)
    val viewModel = storeOwner.viewModelStore.get(
      key = key,
      factory = { ViewModelWithStore(storeFactory(), DefaultDispatchersFacade) }
    )
    return viewModel.store
  }
  
  private fun keyFromProperty(thisRef: ViewModelStoreOwner, property: KProperty<*>): String {
    return thisRef::class.java.canonicalName!! + ":" + property.name
  }
}
