package com.arsvechkarev.vault.features.list.domain

import com.arsvechkarev.vault.core.PasswordFilesSaver
import org.json.JSONArray

class PasswordsListRepository(private val saver: PasswordFilesSaver) {
  
  fun getAllPasswords(masterPassword: String): JSONArray {
    return saver.getDecryptedPasswordsList(masterPassword)
  }
  
  fun savePassword(masterPassword: String, s: String, s1: String) {
    saver.savePassword(masterPassword, s, s1)
  }
}