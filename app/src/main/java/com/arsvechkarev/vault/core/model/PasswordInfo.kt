package com.arsvechkarev.vault.core.model

import com.arsvechkarev.vault.recycler.DifferentiableItem

data class PasswordInfo(
  val serviceName: String,
  val password: String,
) : DifferentiableItem {
  override val id = serviceName
}