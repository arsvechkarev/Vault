package com.arsvechkarev.vault.features.common.model

import com.arsvechkarev.vault.recycler.DifferentiableItem

data class Title(
  val type: Type
) : DifferentiableItem {
  override val id = type.toString()
  
  enum class Type {
    FAVORITES, PASSWORDS, NOTES
  }
}

object Loading : DifferentiableItem {
  override val id: String = javaClass.name
  override fun equals(other: Any?): Boolean {
    return other is Loading
  }
}

object Empty : DifferentiableItem {
  override val id: String = javaClass.name
  override fun equals(other: Any?): Boolean {
    return other is Empty
  }
}

object EmptySearch : DifferentiableItem {
  override val id: String = javaClass.name
  override fun equals(other: Any?): Boolean {
    return other is EmptySearch
  }
}
