package com.arsvechkarev.vault.features.common

data class TextState(
  val initialText: String,
  val editedText: String = initialText,
  val isEditingNow: Boolean = false
)

fun TextState.reset(): TextState {
  return copy(editedText = initialText, isEditingNow = false)
}

fun TextState.update(newText: String): TextState {
  return copy(initialText = newText, editedText = newText, isEditingNow = false)
}

fun TextState.edit(newText: String): TextState {
  return copy(editedText = newText.trim(), isEditingNow = true)
}