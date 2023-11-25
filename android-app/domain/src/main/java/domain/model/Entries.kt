package domain.model

import domain.Password

sealed interface BaseEntry

data class PasswordEntry(
  val id: String,
  val title: String,
  val username: String,
  val password: Password,
  val url: String,
  val notes: String,
  val isFavorite: Boolean,
) : BaseEntry

data class NoteEntry(
  val id: String,
  val title: String,
  val text: String,
  val isFavorite: Boolean
) : BaseEntry
