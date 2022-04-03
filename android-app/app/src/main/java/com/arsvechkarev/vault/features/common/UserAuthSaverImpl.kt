package com.arsvechkarev.vault.features.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserAuthSaverImpl @Inject constructor(context: Context) : UserAuthSaver {

    private val sharedPreferences = context.getSharedPreferences(USER_AUTH_FILENAME, MODE_PRIVATE)

    override fun isUserAuthorized(): Boolean {
        return sharedPreferences.getBoolean(IS_USER_AUTHORIZED_KEY, false)
    }

    @SuppressLint("ApplySharedPref")
    override fun setUserIsAuthorized(authorized: Boolean) {
        sharedPreferences.edit().putBoolean(IS_USER_AUTHORIZED_KEY, authorized).commit()
    }

    companion object {

        const val IS_USER_AUTHORIZED_KEY = "is_user_authorized_key"
        const val USER_AUTH_FILENAME = "user_auth_name"
    }
}