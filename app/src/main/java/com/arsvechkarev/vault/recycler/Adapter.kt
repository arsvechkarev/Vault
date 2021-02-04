package com.arsvechkarev.vault.recycler

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlin.reflect.KClass

abstract class Adapter : RecyclerView.Adapter<ViewHolder>() {
  
  protected var data: List<DisplayableItem> = ArrayList()
  
  private val classesToViewTypes = HashMap<KClass<*>, Int>()
  private val delegatesSparseArray = SparseArrayCompat<AdapterDelegate<out DisplayableItem>>()
  private val delegates = ArrayList<AdapterDelegate<out DisplayableItem>>()
  
  protected fun addDelegates(vararg delegates: AdapterDelegate<out DifferentiableItem>) {
    this.delegates.addAll(delegates)
    delegates.forEachIndexed { i, delegate ->
      classesToViewTypes[delegate.modelClass] = i
      delegatesSparseArray.put(i, delegate)
    }
  }
  
  fun submitList(list: List<DisplayableItem>?, notify: Boolean = true) {
    data = list ?: ArrayList()
    if (notify) notifyDataSetChanged()
  }
  
  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    delegates.forEach { it.onAttachedToRecyclerView(recyclerView) }
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val delegate = delegatesSparseArray[viewType] ?: error("No delegate for view type $viewType")
    return delegate.onCreateViewHolder(parent)
  }
  
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val adapterDelegate = delegatesSparseArray[getItemViewType(position)]
        ?: error("No delegate for position $position")
    adapterDelegate.onBindViewHolderRaw(holder, data[position])
  }
  
  override fun getItemViewType(position: Int): Int {
    return classesToViewTypes[data[position]::class] ?: error(
      "Can't find delegate for position: $position")
  }
  
  override fun getItemCount(): Int {
    return data.size
  }
  
  override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
    super.onDetachedFromRecyclerView(recyclerView)
    delegates.forEach { it.onDetachedFromRecyclerView(recyclerView) }
  }
}