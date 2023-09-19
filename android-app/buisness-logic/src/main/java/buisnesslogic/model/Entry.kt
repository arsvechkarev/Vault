package buisnesslogic.model

import com.google.gson.annotations.SerializedName

sealed interface Entry

data class PasswordEntry(
  @SerializedName("id")
  val id: String,
  @SerializedName("website")
  val websiteName: String,
  @SerializedName("login")
  val login: String,
  @SerializedName("password")
  // TODO (9/19/23): Change to Password class
  val password: String,
  @SerializedName("notes")
  val notes: String,
) : Entry

data class CreditCard(
  @SerializedName("id")
  val id: String,
  @SerializedName("cardNumber")
  val cardNumber: String,
  @SerializedName("expirationDate")
  val expirationDate: String,
  @SerializedName("cardholderName")
  val cardholderName: String,
  @SerializedName("cvcCode")
  val cvcCode: String,
  @SerializedName("pinCode")
  val pinCode: String,
  @SerializedName("notes")
  val notes: String
) : Entry

data class PlainText(
  @SerializedName("id")
  val id: String,
  @SerializedName("title")
  val title: String,
  @SerializedName("text")
  val text: String
) : Entry
