package com.arsvechkarev.vault.features.creating_password

import buisnesslogic.model.PasswordCharacteristic
import buisnesslogic.model.PasswordCharacteristic.NUMBERS
import buisnesslogic.model.PasswordCharacteristic.SPECIAL_SYMBOLS
import buisnesslogic.model.PasswordCharacteristic.UPPERCASE_SYMBOLS
import com.arsvechkarev.vault.core.Router
import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommand.CheckPasswordStrength
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommand.ComputePasswordCharacteristics
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommand.ConfirmSavePassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommand.GeneratePassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordEvent.ComputedPasswordCharacteristics
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordEvent.GeneratedPassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordEvent.HideLoadingDialog
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordEvent.PasswordStrengthChanged
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordEvent.Setup
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordEvent.ShowLoadingDialog
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordNews.ShowGeneratedPassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent.Setup.PasswordConfigurationMode.EditPassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent.Setup.PasswordConfigurationMode.NewPassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnBackClicked
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnConfirmPasswordSavingClicked
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnDeclinePasswordSavingClicked
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnGeneratePasswordClicked
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnPasswordChanged
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnPasswordLengthChanged
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnSavePasswordClicked
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnToggledNumbers
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnToggledSpecialSymbols
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnToggledUppercaseSymbols
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.SetupCompleted
import java.util.EnumSet

private typealias CPState = CreatingPasswordState

class CreatingPasswordReducer(
  private val router: Router
) : DslReducer<CPState, CreatingPasswordEvent, CreatingPasswordCommand, CreatingPasswordNews>() {
  
  override fun dslReduce(event: CreatingPasswordEvent) {
    when (event) {
      is CreatingPasswordUiEvent -> handleUiEvent(event)
      is Setup -> {
        state { copy(mode = event.mode) }
      }
      is GeneratedPassword -> {
        state { copy(password = event.password) }
        news(ShowGeneratedPassword(event.password))
      }
      is PasswordStrengthChanged -> {
        state { copy(passwordStrength = event.strength) }
      }
      is ComputedPasswordCharacteristics -> {
        state {
          copy(
            uppercaseSymbolsEnabled = event.characteristics.contains(UPPERCASE_SYMBOLS),
            numbersEnabled = event.characteristics.contains(NUMBERS),
            specialSymbolsEnabled = event.characteristics.contains(SPECIAL_SYMBOLS)
          )
        }
      }
      ShowLoadingDialog -> {
        state { copy(showLoadingDialog = true) }
      }
      HideLoadingDialog -> {
        state { copy(showLoadingDialog = false) }
      }
    }
  }
  
  private fun handleUiEvent(event: CreatingPasswordUiEvent) {
    when (event) {
      SetupCompleted -> {
        state { copy(setupCompleted = true) }
        when (val mode = state.mode) {
          NewPassword -> {
            val characteristics = EnumSet.allOf(PasswordCharacteristic::class.java)
            commands(GeneratePassword(state.passwordLength, characteristics))
          }
          is EditPassword -> {
            state { copy(password = mode.password) }
            commands(
              CheckPasswordStrength(mode.password),
              ComputePasswordCharacteristics(mode.password),
            )
          }
          else -> Unit
        }
      }
      is OnPasswordChanged -> {
        state { copy(password = event.password) }
        commands(
          CheckPasswordStrength(event.password),
          ComputePasswordCharacteristics(event.password)
        )
      }
      is OnPasswordLengthChanged -> {
        state { copy(passwordLength = event.length) }
      }
      OnGeneratePasswordClicked -> {
        val characteristics = EnumSet.noneOf(PasswordCharacteristic::class.java)
        if (state.uppercaseSymbolsEnabled) characteristics.add(UPPERCASE_SYMBOLS)
        if (state.numbersEnabled) characteristics.add(NUMBERS)
        if (state.specialSymbolsEnabled) characteristics.add(SPECIAL_SYMBOLS)
        commands(GeneratePassword(state.passwordLength, characteristics))
      }
      OnSavePasswordClicked -> {
        if (state.password.isNotBlank()) {
          state { copy(showConfirmationDialog = true) }
        }
      }
      OnDeclinePasswordSavingClicked -> {
        state { copy(showConfirmationDialog = false) }
      }
      OnToggledUppercaseSymbols -> {
        state { copy(uppercaseSymbolsEnabled = !state.uppercaseSymbolsEnabled) }
      }
      OnToggledNumbers -> {
        state { copy(numbersEnabled = !state.numbersEnabled) }
      }
      OnToggledSpecialSymbols -> {
        state { copy(specialSymbolsEnabled = !state.specialSymbolsEnabled) }
      }
      OnConfirmPasswordSavingClicked -> {
        commands(ConfirmSavePassword(state.password))
      }
      OnBackClicked -> {
        if (state.showConfirmationDialog) {
          state { copy(showConfirmationDialog = false) }
        } else {
          router.goBack()
        }
      }
    }
  }
}
