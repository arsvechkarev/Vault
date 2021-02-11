package com.arsvechkarev.vault.features.common

interface UserAuthSaver {
  
  fun setUserIsAuthorized(authorized: Boolean)
  
  fun isUserAuthorized(): Boolean
}