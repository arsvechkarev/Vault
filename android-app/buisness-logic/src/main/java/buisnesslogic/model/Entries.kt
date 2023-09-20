package buisnesslogic.model

import buisnesslogic.Password

sealed interface BaseEntry

data class PasswordEntry(
  val id: String,
  val title: String,
  val username: String,
  val password: Password,
  val url: String,
  val notes: String,
) : BaseEntry

data class CreditCardEntry(
  val id: String,
  val cardNumber: String,
  val expirationDate: String,
  val cardholderName: String,
  val cvcCode: String,
  val pinCode: String,
  val notes: String
) : BaseEntry

data class PlainTextEntry(
  val id: String,
  val title: String,
  val text: String
) : BaseEntry
