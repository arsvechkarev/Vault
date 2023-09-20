package com.arsvechkarev.vault.features.common.model

import com.arsvechkarev.vault.recycler.DifferentiableItem

sealed interface EntryItem : DifferentiableItem

data class PasswordItem(
  override val id: String,
  val title: String,
  val username: String,
  val hasActualTitle: Boolean
) : EntryItem

data class PlainTextItem(
  override val id: String,
  val title: String,
  val hasActualTitle: Boolean
) : EntryItem
