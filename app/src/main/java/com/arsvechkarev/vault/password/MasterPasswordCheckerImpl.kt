package com.arsvechkarev.vault.password

import android.annotation.SuppressLint
import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.arsvechkarev.vault.core.decryptText
import com.arsvechkarev.vault.core.encryptText

class MasterPasswordCheckerImpl(context: Context) : MasterPasswordChecker {
  
  private val sharedPrefs = EncryptedSharedPreferences.create(
    context,
    MASTER_PASSWORD_CHECK_FILENAME,
    MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
  )
  
  @SuppressLint("ApplySharedPref")
  override fun encodeSecretPhrase(masterPassword: String) {
    val encryptedPhrase = encryptText(masterPassword, CHECK_PHRASE)
    sharedPrefs.edit().putString(MASTER_PASSWORD_PHRASE_KEY, encryptedPhrase).commit()
  }
  
  override fun isCorrect(masterPassword: String): Boolean {
    val encryptedPhrase = sharedPrefs.getString(MASTER_PASSWORD_PHRASE_KEY, null)!!
    return try {
      val decryptedPhrase = decryptText(masterPassword, encryptedPhrase)
      decryptedPhrase == CHECK_PHRASE
    } catch (e: Throwable) {
      false
    }
  }
  
  companion object {
    
    const val CHECK_PHRASE = "This is a phrase to check."
    const val MASTER_PASSWORD_CHECK_FILENAME = "master_password_check"
    const val MASTER_PASSWORD_PHRASE_KEY = "master_password_word_phrase"
  }
}