package com.arsvechkarev.vault.features.creating_password

const val PasswordCreatingTag = "PasswordCreatingTag"

interface PasswordCreatingEvents

/**
 * Reactions, i.e events that [PasswordCreatingScreen] sends and other screens can listen to
 */
sealed class PasswordCreatingReactions : PasswordCreatingEvents {
  
  /** Happens when user clicks on 'save' button */
  class OnSavePasswordButtonClicked(val password: String) : PasswordCreatingReactions()
  
  /** Happens when user clicks 'accept' button in AcceptPasswordDialog */
  class OnNewPasswordAccepted(val password: String) : PasswordCreatingReactions()
}

/**
 * Actions, i.e events that other screens send to [PasswordCreatingScreen]
 */
sealed class PasswordCreatingActions : PasswordCreatingEvents {
  
  /**
   * Action that configures [PasswordCreatingScreen]
   */
  sealed class ConfigureMode : PasswordCreatingActions() {
    
    object NewPassword : ConfigureMode()
    
    class EditPassword(val password: String) : ConfigureMode()
  }
  
  object ShowAcceptPasswordDialog : PasswordCreatingActions()
  
  object ShowLoading : PasswordCreatingActions()
  
  object ExitScreen : PasswordCreatingActions()
}