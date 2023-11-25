package domain.model

import domain.Password

class PasswordEntryData(
  val title: String,
  val username: String,
  val password: Password,
  val url: String,
  val notes: String,
  val isFavorite: Boolean
)

class NoteEntryData(
  val title: String,
  val text: String,
  val isFavorite: Boolean
)
