package com.arsvechkarev.vault.core

/**
 * Helps with user authorization
 */
interface UserAuthSaver {
  
  fun setUserIsAuthorized(authorized: Boolean)
  
  fun isUserAuthorized(): Boolean
}