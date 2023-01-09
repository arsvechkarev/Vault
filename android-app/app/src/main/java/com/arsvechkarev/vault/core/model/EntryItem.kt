package com.arsvechkarev.vault.core.model

import android.os.Parcelable
import com.arsvechkarev.vault.recycler.DifferentiableItem
import kotlinx.parcelize.Parcelize

sealed interface EntryItem

@Parcelize
data class PasswordItem(
  override val id: String,
  val websiteName: String,
  val login: String,
  val password: String,
  val notes: String,
) : EntryItem, DifferentiableItem, Parcelable

@Parcelize
data class CreditCardItem(
  override val id: String,
  val cardNumber: String,
  val expirationDate: String,
  val cardholderName: String,
  val cvcCode: String,
  val pinCode: String,
  val notes: String,
) : EntryItem, DifferentiableItem, Parcelable

@Parcelize
data class PlainTextItem(
  override val id: String,
  val title: String,
  val text: String,
) : EntryItem, DifferentiableItem, Parcelable
