package com.arsvechkarev.vault.features.common

data class TextState(
  val initialText: String,
  val editedText: String = initialText,
  val isEditingNow: Boolean = false
) {
  
  companion object {
    fun empty() = TextState("")
  }
}

fun TextState.reset(): TextState {
  return copy(editedText = initialText, isEditingNow = false)
}

fun TextState.update(newText: String): TextState {
  return copy(initialText = newText, editedText = newText, isEditingNow = false)
}

fun TextState.edit(newText: String): TextState {
  return copy(editedText = newText, isEditingNow = true)
}