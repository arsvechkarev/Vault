package com.arsvechkarev.vault.recycler

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.vault.viewdsl.inflate

class SimpleAdapter<T>(
  private val layoutRes: Int,
  private val data: List<T>,
  private val onViewHolderInitialization: (RecyclerView.ViewHolder) -> Unit,
  private val onViewHolderBind: (View, T) -> Unit
) : RecyclerView.Adapter<SimpleAdapter<T>.SimpleViewHolder>() {
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
    return SimpleViewHolder(parent.inflate(layoutRes))
  }
  
  override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
    onViewHolderBind(holder.itemView, data[position])
  }
  
  override fun getItemCount(): Int {
    return data.size
  }
  
  inner class SimpleViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    
    init {
      onViewHolderInitialization(this)
    }
  }
}

class SimpleAdapterBuilder<T> {
  private var layoutRes = -1
  private var data: List<T>? = emptyList()
  private var onViewHolderInitialization: (RecyclerView.ViewHolder) -> Unit = {}
  private var onBindViewHolder: (View, T) -> Unit = { _, _ -> }
  
  fun layoutRes(layoutRes: Int) {
    this.layoutRes = layoutRes
  }
  
  fun data(data: List<T>) {
    this.data = data
  }
  
  fun onViewHolderInitialization(block: (RecyclerView.ViewHolder) -> Unit) {
    this.onViewHolderInitialization = block
  }
  
  fun onBindViewHolder(block: (View, T) -> Unit) {
    this.onBindViewHolder = block
  }
  
  fun build(): SimpleAdapter<T> {
    if (layoutRes == -1) {
      require(layoutRes != -1) { "Layout resource is null" }
    }
    return SimpleAdapter(
      layoutRes, data!!, onViewHolderInitialization, onBindViewHolder
    )
  }
}

fun <T> buildAdapter(block: SimpleAdapterBuilder<T>.() -> Unit): SimpleAdapter<T> {
  return SimpleAdapterBuilder<T>().apply(block).build()
}