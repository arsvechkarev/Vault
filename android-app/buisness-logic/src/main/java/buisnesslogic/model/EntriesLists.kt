package buisnesslogic.model

import com.google.gson.annotations.SerializedName

data class EntriesLists(
  @SerializedName("passwords")
  val passwords: List<PasswordEntry>,
  @SerializedName("plainTexts")
  val plainTextEntries: List<PlainTextEntry>
)
