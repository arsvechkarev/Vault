package com.arsvechkarev.vaultdesktop.model

import com.google.gson.annotations.SerializedName

sealed interface Entry

data class Password(
  @SerializedName("id")
  val id: String,
  @SerializedName("website")
  val websiteName: String,
  @SerializedName("login")
  val login: String,
  @SerializedName("password")
  val password: Password,
  @SerializedName("notes")
  val notes: String,
) : Entry

data class PlainText(
  @SerializedName("id")
  val id: String,
  @SerializedName("title")
  val title: String,
  @SerializedName("text")
  val text: String
) : Entry
