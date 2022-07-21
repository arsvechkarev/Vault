package com.arsvechkarev.vault.core

import buisnesslogic.PasswordsStorage
import com.arsvechkarev.vault.core.model.PasswordInfoItem
import com.arsvechkarev.vault.core.model.toInfoItemsList
import com.arsvechkarev.vault.core.model.toPasswordInfo
import java.util.Locale

class CachedPasswordsStorage(private val storage: PasswordsStorage) {
  
  private var passwords: MutableList<PasswordInfoItem> = ArrayList()
  
  suspend fun getPasswords(masterPassword: String): List<PasswordInfoItem> {
    if (passwords.isEmpty()) {
      passwords = storage.getPasswords(masterPassword).toInfoItemsList().toMutableList()
      sortList()
    }
    return ArrayList(passwords)
  }
  
  suspend fun savePassword(masterPassword: String, passwordInfoItem: PasswordInfoItem) {
    storage.savePassword(masterPassword, passwordInfoItem.toPasswordInfo())
    passwords.add(passwordInfoItem)
    sortList()
  }
  
  suspend fun updatePassword(masterPassword: String, passwordInfoItem: PasswordInfoItem) {
    storage.updatePasswordInfo(masterPassword, passwordInfoItem.toPasswordInfo())
    for (i in 0 until passwords.size) {
      val currentPasswordInfo = passwords[i]
      if (currentPasswordInfo.id == passwordInfoItem.id) {
        passwords[i] = passwordInfoItem
        break
      }
    }
    sortList()
  }
  
  suspend fun deletePassword(masterPassword: String, passwordInfoItem: PasswordInfoItem) {
    storage.deletePassword(masterPassword, passwordInfoItem.toPasswordInfo())
    passwords.remove(passwordInfoItem)
  }
  
  private fun sortList() {
    passwords.sortWith(Comparator { o1, o2 ->
      return@Comparator o1.websiteName.lowercase(Locale.getDefault())
          .compareTo(o2.websiteName.lowercase(Locale.getDefault()))
    })
  }
}
