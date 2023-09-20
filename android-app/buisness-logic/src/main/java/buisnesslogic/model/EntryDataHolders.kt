package buisnesslogic.model

import buisnesslogic.Password

class PasswordEntryData(
  val title: String,
  val username: String,
  val password: Password,
  val url: String,
  val notes: String,
)

class PlainTextEntryData(
  val title: String,
  val text: String
)
