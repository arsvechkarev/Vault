package com.arsvechkarev.vaultdesktop.model

import com.google.gson.annotations.SerializedName

data class Entries(
  @SerializedName("passwords")
  val passwords: List<Password>,
  @SerializedName("plainTexts")
  val plainTexts: List<PlainText>
)
