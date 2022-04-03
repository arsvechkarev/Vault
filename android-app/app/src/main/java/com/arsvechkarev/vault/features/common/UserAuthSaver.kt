package com.arsvechkarev.vault.features.common

/**
 * Helps with user authorization
 */
interface UserAuthSaver {

    fun setUserIsAuthorized(authorized: Boolean)

    fun isUserAuthorized(): Boolean
}