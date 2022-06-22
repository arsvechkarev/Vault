package com.arsvechkarev.vault.common

/**
 * Helps with user authorization
 */
interface UserAuthSaver {
  
  fun setUserIsAuthorized(authorized: Boolean)
  
  fun isUserAuthorized(): Boolean
}