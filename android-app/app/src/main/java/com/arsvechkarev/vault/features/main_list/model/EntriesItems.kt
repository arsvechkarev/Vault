package com.arsvechkarev.vault.features.main_list.model

import com.arsvechkarev.vault.core.model.PasswordItem

// TODO (1/9/2023): Add plain texts and credit cards support
class EntriesItems(
  val passwordsItems: List<PasswordItem>,
) {
  
  fun isNotEmpty(): Boolean {
    return passwordsItems.isNotEmpty()
  }
}