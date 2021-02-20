package com.arsvechkarev.vault.core.model

import android.os.Parcelable
import com.arsvechkarev.vault.recycler.DifferentiableItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Service(
  override val id: String,
  val serviceName: String,
  val username: String,
  val email: String,
  val password: String,
) : DifferentiableItem, Parcelable