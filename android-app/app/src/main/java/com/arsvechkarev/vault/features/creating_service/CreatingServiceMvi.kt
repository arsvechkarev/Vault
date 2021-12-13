package com.arsvechkarev.vault.features.creating_service

sealed class CreatingServiceActions {
  
  object ShowServiceNameCannotBeEmpty : CreatingServiceActions()
}

sealed class CreatingServiceUserActions : CreatingServiceActions() {
  
  class OnServiceNameTextChanged(val text: String) : CreatingServiceUserActions()
  
  class OnContinueClicked(
    val serviceName: String, val username: String, val email: String
  ) : CreatingServiceUserActions()
  
  object OnBackPressed : CreatingServiceUserActions()
}

data class CreatingServiceState(
  val serviceName: String = "",
  val username: String = "",
  val email: String = "",
  val showServiceIconCannotBeEmpty: Boolean = false
)