package com.arsvechkarev.vault.features.common.domain

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE

class AuthCheckerImpl constructor(context: Context) : AuthChecker {
  
  private val sharedPreferences = context.getSharedPreferences(USER_AUTH_FILENAME, MODE_PRIVATE)
  
  override fun isUserLoggedIn(): Boolean {
    return sharedPreferences.getBoolean(IS_USER_AUTHORIZED_KEY, false)
  }
  
  @SuppressLint("ApplySharedPref")
  override fun setUserIsLoggedIn() {
    sharedPreferences.edit().putBoolean(IS_USER_AUTHORIZED_KEY, true).commit()
  }
  
  companion object {
    
    const val IS_USER_AUTHORIZED_KEY = "is_user_authorized_key"
    const val USER_AUTH_FILENAME = "user_auth_name"
  }
}
