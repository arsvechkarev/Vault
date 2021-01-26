package com.arsvechkarev.vault.core.model

import com.arsvechkarev.vault.recycler.DifferentiableItem

data class ServiceInfo(
  val serviceName: String,
  val password: String,
) : DifferentiableItem {
  override val id = serviceName
}