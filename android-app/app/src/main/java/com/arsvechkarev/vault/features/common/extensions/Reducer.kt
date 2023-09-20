package com.arsvechkarev.vault.features.common.extensions

import buisnesslogic.model.BaseEntry
import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.common.TextState
import com.arsvechkarev.vault.features.common.reset


fun <Item : BaseEntry, State : Any, Command : Any,
    News : Any> DslReducer<State, *, Command, News>.handleAction(
  itemProvider: () -> Item,
  textState: TextState,
  stateResetAction: (TextState) -> State,
  updateAction: Item.(String) -> Item,
  updateCommand: (Item) -> Command,
  allowEmptySave: Boolean = false,
  copyCommand: (text: String) -> Command,
  copyNews: News,
  setTextNews: (String) -> News,
) {
  with(textState) {
    if (isEditingNow) {
      if (!allowEmptySave && editedText.isBlank()) {
        return
      }
      val trimmedText = editedText.trim()
      if (trimmedText == initialText) {
        state { stateResetAction(textState.reset()) }
      } else {
        commands(updateCommand(updateAction(itemProvider(), trimmedText)))
        news(setTextNews(trimmedText))
      }
    } else {
      commands(copyCommand(editedText))
      news(copyNews)
    }
  }
}