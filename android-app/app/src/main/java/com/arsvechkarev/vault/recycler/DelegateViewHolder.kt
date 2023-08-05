package com.arsvechkarev.vault.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class DelegateViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
  
  open fun bind(item: T) = Unit
  
  open fun onViewRecycled() = Unit
}