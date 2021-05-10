package com.arsvechkarev.vault.recycler

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlin.reflect.KClass

abstract class BaseListAdapter(
  private var onReadyToLoadNextPage: (() -> Unit)? = null
) : ListAdapter<DifferentiableItem, ViewHolder>(DiffCallback()) {
  
  protected var recyclerView: RecyclerView? = null
    private set
  
  private val classesToViewTypes = HashMap<KClass<*>, Int>()
  private val delegatesSparseArray = SparseArrayCompat<ListAdapterDelegate<out DifferentiableItem>>()
  private val delegates = ArrayList<ListAdapterDelegate<out DifferentiableItem>>()
  
  protected fun addDelegates(vararg delegates: ListAdapterDelegate<out DifferentiableItem>) {
    this.delegates.addAll(delegates)
    delegates.forEachIndexed { i, delegate ->
      classesToViewTypes[delegate.modelClass] = i
      delegatesSparseArray.put(i, delegate)
    }
  }
  
  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    this.recyclerView = recyclerView
    delegates.forEach { it.onAttachedToRecyclerView(recyclerView) }
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val delegate = delegatesSparseArray[viewType] ?: error("No delegate for view type $viewType")
    return delegate.onCreateViewHolder(parent)
  }
  
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    if (position == itemCount - 5) {
      onReadyToLoadNextPage?.invoke()
    }
    val adapterDelegate = delegatesSparseArray[getItemViewType(position)]
        ?: error("No delegate for position $position")
    adapterDelegate.onBindViewHolderRaw(holder, getItem(position))
  }
  
  override fun getItemViewType(position: Int): Int {
    return classesToViewTypes[getItem(position)::class] ?: error(
      "Can't find delegate for position: $position")
  }
  
  override fun onViewRecycled(holder: ViewHolder) {
    (holder as? DelegateViewHolder<*>)?.onViewRecycled()
  }
  
  override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
    delegates.forEach { it.onDetachedFromRecyclerView(recyclerView) }
    onReadyToLoadNextPage = null
  }
}