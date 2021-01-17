package com.arsvechkarev.vault.password

import android.annotation.SuppressLint
import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class MasterPasswordSaverImpl(context: Context) : MasterPasswordSaver {
  
  private val encryptedSharedPrefs = EncryptedSharedPreferences.create(
    context,
    MASTER_PASSWORD_FILENAME,
    MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
  )
  
  override fun getSavedMasterPassword(): String? {
    return encryptedSharedPrefs.getString(MASTER_PASSWORD_KEY, null)
  }
  
  @SuppressLint("ApplySharedPref")
  override fun saveMasterPassword(masterPassword: String) {
    encryptedSharedPrefs.edit().putString(MASTER_PASSWORD_KEY, masterPassword).commit()
  }
  
  companion object {
    
    const val MASTER_PASSWORD_FILENAME = "master_password_file"
    const val MASTER_PASSWORD_KEY = "master_password"
  }
}