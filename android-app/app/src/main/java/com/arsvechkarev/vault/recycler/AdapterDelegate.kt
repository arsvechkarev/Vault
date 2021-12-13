package com.arsvechkarev.vault.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KClass

abstract class AdapterDelegate<T : DisplayableItem>(val modelClass: KClass<T>) {
  
  @Suppress("UNCHECKED_CAST")
  internal fun onBindViewHolderRaw(holder: RecyclerView.ViewHolder, item: DisplayableItem) {
    onBindViewHolder(holder as DelegateViewHolder<T>, item as T)
  }
  
  abstract fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<T>
  
  abstract fun onBindViewHolder(holder: DelegateViewHolder<T>, item: T)
  
  open fun onAttachedToRecyclerView(recyclerView: RecyclerView) {}
  
  open fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {}
}