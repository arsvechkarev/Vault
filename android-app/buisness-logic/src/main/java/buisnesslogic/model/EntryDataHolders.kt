package buisnesslogic.model

import buisnesslogic.Password

class PasswordEntryData(
  val title: String,
  val username: String,
  val password: Password,
  val url: String,
  val notes: String,
  val isFavorite: Boolean
)

class PlainTextEntryData(
  val title: String,
  val text: String,
  val isFavorite: Boolean
)
