package com.arsvechkarev.vault.recycler

import androidx.recyclerview.widget.DiffUtil

/**
 * Callback type for diff util
 */
enum class CallbackType {
  APPENDED_LIST, TWO_LISTS, ALWAYS_FALSE
}

/**
 * DiffCallback that used when data is just appended to the list, but the rest
 * isn't changed
 */
class AppendedListDiffCallbacks(
  private val newList: List<DifferentiableItem>,
  private val oldListLength: Int
) : DiffUtil.Callback() {
  
  override fun getOldListSize(): Int = oldListLength
  
  override fun getNewListSize(): Int = newList.size
  
  override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
    return newList[oldPosition].id == newList[newPosition].id
  }
  
  override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
    return newList[oldPosition] == newList[newPosition]
  }
}

/**
 * DiffCallback that uses old list and new list to compare elements
 */
class TwoListsDiffCallBack(
  private val oldList: List<DifferentiableItem>,
  private val newList: List<DifferentiableItem>,
) : DiffUtil.Callback() {
  
  override fun getOldListSize() = oldList.size
  
  override fun getNewListSize() = newList.size
  
  override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
    return oldList[oldPosition].id == newList[newPosition].id
  }
  
  override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
    return oldList[oldPosition] == newList[newPosition]
  }
}

/**
 * DiffCallback that always returns false for every item
 */
class AlwaysFalseCallback(
  private val oldList: List<DifferentiableItem>,
  private val newList: List<DifferentiableItem>,
) : DiffUtil.Callback() {
  
  override fun getOldListSize() = oldList.size
  
  override fun getNewListSize() = newList.size
  
  override fun areItemsTheSame(oldPosition: Int, newPosition: Int) = false
  
  override fun areContentsTheSame(oldPosition: Int, newPosition: Int) = false
}