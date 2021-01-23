package com.arsvechkarev.vault.recycler

import androidx.recyclerview.widget.RecyclerView

/**
 * Item to be displayed in [RecyclerView]
 */
interface DifferentiableItem : DisplayableItem {
  
  /**
   * Id to distinguish two different elements
   */
  val id: String
  
  /**
   * Every class inherits from [DifferentiableItem] should override equals in order to compare elements
   * properly
   */
  override fun equals(other: Any?): Boolean
}