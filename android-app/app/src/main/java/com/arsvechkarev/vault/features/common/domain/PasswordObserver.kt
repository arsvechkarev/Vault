package com.arsvechkarev.vault.features.common.domain

import domain.Password
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface PasswordObserver {
  
  val passwordChanges: Flow<Password>
  
  suspend fun changePassword(password: Password)
}

class PasswordObserverImpl : PasswordObserver {
  
  private val _passwordChanges = MutableSharedFlow<Password>()
  
  override val passwordChanges: Flow<Password> get() = _passwordChanges
  
  override suspend fun changePassword(password: Password) {
    _passwordChanges.emit(password)
  }
}
