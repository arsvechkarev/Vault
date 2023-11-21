package com.arsvechkarev.vault.core

import com.arsvechkarev.vault.recycler.DifferentiableItem

/**
 * Result class that facilitates working with different result types, such as Success,
 * Empty, Loading
 *
 * @param S type of success result
 *
 * @see Type
 */
class ListScreenState private constructor(
  private val data: List<DifferentiableItem>,
  private val type: Type
) {
  
  val isEmpty get() = type == Type.EMPTY
  
  val isNotEmpty get() = !isEmpty
  
  fun getItems(
    loadingItems: () -> List<DifferentiableItem>,
    emptyItems: () -> List<DifferentiableItem>,
  ): List<DifferentiableItem> {
    return when (type) {
      Type.SUCCESS -> data
      Type.LOADING -> loadingItems()
      Type.EMPTY -> emptyItems()
    }
  }
  
  fun getSuccessItemsOrEmpty(): List<DifferentiableItem> {
    return if (type == Type.SUCCESS && data.isNotEmpty()) data else emptyList()
  }
  
  companion object {
    
    fun loading(): ListScreenState = ListScreenState(emptyList(), Type.LOADING)
    
    fun success(value: List<DifferentiableItem>): ListScreenState = ListScreenState(value,
      Type.SUCCESS)
    
    fun empty(): ListScreenState = ListScreenState(emptyList(), Type.EMPTY)
  }
}

/**
 * Represents a result type that [ListScreenState] could have
 */
private enum class Type {
  SUCCESS, LOADING, EMPTY
}
