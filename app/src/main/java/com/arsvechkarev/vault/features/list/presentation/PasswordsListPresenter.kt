package com.arsvechkarev.vault.features.list.presentation

import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.JSON_SERVICE_NAME
import com.arsvechkarev.vault.core.JSON_SERVICE_PASSWORD
import com.arsvechkarev.vault.core.concurrency.Threader
import com.arsvechkarev.vault.core.model.PasswordInfo
import com.arsvechkarev.vault.features.list.domain.PasswordsListRepository

class PasswordsListPresenter(
  threader: Threader,
  private val passwordsListRepository: PasswordsListRepository
) : BasePresenter<PasswordsListView>(threader) {
  
  private val masterPassword = "thisismypassword"
  
  fun loadPasswords() {
    onIoThread {
      try {
        val passwords = passwordsListRepository.getAllPasswords(masterPassword)
        if (passwords.length() == 0) {
          updateViewState { showNoPasswords() }
        } else {
          val list = ArrayList<PasswordInfo>()
          for (i in 0 until passwords.length()) {
            val obj = passwords.getJSONObject(i)
            list.add(PasswordInfo(
              obj.getString(JSON_SERVICE_NAME),
              obj.getString(JSON_SERVICE_PASSWORD),
            ))
            updateViewState { showPasswordsList(list) }
          }
        }
      } catch (e: Throwable) {
        e.printStackTrace()
      }
    }
  }
  
  fun savePassword(s: String, s1: String) {
    passwordsListRepository.savePassword(masterPassword, s, s1)
    println("qwerty: saved")
  }
}