package com.arsvechkarev.vault.features.common.model

import com.arsvechkarev.vault.recycler.DifferentiableItem

data class Title(
  val titleRes: Int
) : DifferentiableItem {
  override val id = titleRes.toString()
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
