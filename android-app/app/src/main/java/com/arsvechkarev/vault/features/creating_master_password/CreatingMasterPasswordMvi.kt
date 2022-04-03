package com.arsvechkarev.vault.features.creating_master_password

import buisnesslogic.PasswordStatus
import buisnesslogic.PasswordStrength

sealed class CreatingMasterPasswordScreenActions {

    class PasswordEnteringStateChanged(
        val state: PasswordEnteringState
    ) : CreatingMasterPasswordScreenActions()

    class UpdatePasswordStatus(
        val passwordStatus: PasswordStatus?
    ) : CreatingMasterPasswordScreenActions()

    class UpdatePasswordStrength(
        val passwordStrength: PasswordStrength?
    ) : CreatingMasterPasswordScreenActions()

    object ShowPasswordsMatch : CreatingMasterPasswordScreenActions()

    object ShowPasswordsDontMatch : CreatingMasterPasswordScreenActions()
}

sealed class CreatingMasterPasswordScreenUserActions : CreatingMasterPasswordScreenActions() {

    class OnInitialPasswordTyping(val password: String) : CreatingMasterPasswordScreenUserActions()

    class OnRepeatPasswordTyping(val password: String) : CreatingMasterPasswordScreenUserActions()

    object RequestShowPasswordStrengthDialog : CreatingMasterPasswordScreenUserActions()

    object RequestHidePasswordStrengthDialog : CreatingMasterPasswordScreenUserActions()

    object OnContinueClicked : CreatingMasterPasswordScreenUserActions()

    object OnBackPressed : CreatingMasterPasswordScreenUserActions()

    object OnBackButtonClicked : CreatingMasterPasswordScreenUserActions()
}

sealed class CreatingMasterPasswordSingleEvents {

    object HideErrorText : CreatingMasterPasswordSingleEvents()

    object FinishingAuthorization : CreatingMasterPasswordSingleEvents()
}

data class CreatingMasterPasswordScreenState(
    val passwordEnteringState: PasswordEnteringState = PasswordEnteringState.INITIAL,
    val passwordStatus: PasswordStatus? = null,
    val passwordStrength: PasswordStrength? = null,
    val showPasswordStrengthDialog: Boolean = false,
    val initialPassword: String = "",
    val repeatedPassword: String = "",
    val passwordsMatch: Boolean? = null
)

enum class PasswordEnteringState { INITIAL, REPEATING }
