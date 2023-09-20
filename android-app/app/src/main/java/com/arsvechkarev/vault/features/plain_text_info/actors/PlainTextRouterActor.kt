package com.arsvechkarev.vault.features.plain_text_info.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.navigation.RouterActor
import com.arsvechkarev.vault.features.plain_text_info.PlainTextCommand
import com.arsvechkarev.vault.features.plain_text_info.PlainTextCommand.GoBack
import com.arsvechkarev.vault.features.plain_text_info.PlainTextEvent

fun PlainTextRouterActor(
  router: Router
): Actor<PlainTextCommand, PlainTextEvent> {
  return RouterActor<PlainTextCommand, GoBack, PlainTextEvent>(router) {
    goBack()
  }
}