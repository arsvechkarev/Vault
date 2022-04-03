package com.arsvechkarev.vault.features.settings

sealed class SettingsScreenActions {

    class ShowFingerprintEnteringEnabled(val enabled: Boolean) : SettingsScreenActions()

    object ShowPasswordCheckingDialog : SettingsScreenActions()

    object HidePasswordCheckingDialog : SettingsScreenActions()
}

sealed class SettingsScreenUserActions : SettingsScreenActions() {

    object OnUserFingerprintTextClicked : SettingsScreenUserActions()

    class ToggleUseFingerprintForEnteringCheckbox(val enabled: Boolean) :
        SettingsScreenUserActions()

    object OnBackPressed : SettingsScreenUserActions()
}

sealed class SettingsScreenSingleEvents {

    object ShowBiometricsAddedSuccessfully : SettingsScreenSingleEvents()
}

data class SettingsScreenState(
    val fingerprintEnteringEnabled: Boolean = false,
    val showPasswordCheckingDialog: Boolean = false
)
