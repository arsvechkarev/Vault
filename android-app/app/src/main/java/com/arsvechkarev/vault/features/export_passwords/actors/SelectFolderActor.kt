package com.arsvechkarev.vault.features.export_passwords.actors

import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsCommand
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance

class SelectFolderActor(
  private val router: Router
) : Actor<ExportPasswordsCommand, ExportPasswordsEvent> {
  
  override fun handle(commands: Flow<ExportPasswordsCommand>): Flow<ExportPasswordsEvent> {
    return commands.filterIsInstance()
    //        .emptyMap {
    //        }
  }
}
