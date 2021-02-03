package com.arsvechkarev.vault.core.model

import android.os.Parcelable
import com.arsvechkarev.vault.recycler.DifferentiableItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServiceInfo(
  override val id: String,
  val name: String,
  val email: String,
  val password: String,
) : DifferentiableItem, Parcelable