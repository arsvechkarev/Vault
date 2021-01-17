package com.arsvechkarev.vault.core

interface UserAuthSaver {
  
  fun setUserIsAuthorized(authorized: Boolean)
  
  fun isUserAuthorized(): Boolean
}