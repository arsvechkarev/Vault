package com.arsvechkarev.vault.features.common.data.storage

import buisnesslogic.IdGenerator
import buisnesslogic.PasswordsStorage
import com.arsvechkarev.vault.core.model.PasswordInfoItem
import com.arsvechkarev.vault.core.model.toInfoItemsList
import com.arsvechkarev.vault.core.model.toPasswordInfo
import com.arsvechkarev.vault.core.model.toPasswordInfoList
import java.util.Locale

class CachedPasswordsStorage(
  private val storage: PasswordsStorage,
  private val idGenerator: IdGenerator,
) {
  
  private var passwords: MutableList<PasswordInfoItem> = ArrayList()
  
  suspend fun getPasswords(masterPassword: String): List<PasswordInfoItem> {
    if (passwords.isEmpty()) {
      passwords = storage.getPasswords(masterPassword).toInfoItemsList().toMutableList()
      sortList()
    }
    return ArrayList(passwords)
  }
  
  suspend fun savePasswords(masterPassword: String, passwords: List<PasswordInfoItem>) {
    this.passwords = passwords.toMutableList()
    storage.savePasswords(masterPassword, passwords.toPasswordInfoList())
  }
  
  suspend fun savePassword(
    masterPassword: String,
    websiteName: String,
    login: String,
    password: String
  ): PasswordInfoItem {
    val id = getUniqueId()
    val item = PasswordInfoItem(id, websiteName, login, password, notes = "")
    storage.savePassword(masterPassword, item.toPasswordInfo())
    passwords.add(item)
    sortList()
    return item
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
  
  private fun getUniqueId(): String {
    while (true) {
      val id = idGenerator.generateRandomId()
      if (passwords.any { it.id == id }) {
        continue
      }
      return id
    }
  }
}
