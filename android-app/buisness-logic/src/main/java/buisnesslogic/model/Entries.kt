package buisnesslogic.model

import com.google.gson.annotations.SerializedName

data class Entries(
  @SerializedName("passwords")
  val passwords: List<PasswordEntry>,
  @SerializedName("creditCards")
  val creditCards: List<CreditCard>,
  @SerializedName("plainTexts")
  val plainTexts: List<PlainText>
)
