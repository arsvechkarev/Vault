package com.arsvechkarev.vault.recycler

import androidx.recyclerview.widget.DiffUtil

class DiffCallback : DiffUtil.ItemCallback<DifferentiableItem>() {
  
  override fun areItemsTheSame(
    oldItem: DifferentiableItem,
    newItem: DifferentiableItem
  ): Boolean {
    return oldItem.id == newItem.id
  }
  
  override fun areContentsTheSame(
    oldItem: DifferentiableItem,
    newItem: DifferentiableItem
  ): Boolean {
    return oldItem == newItem
  }
}