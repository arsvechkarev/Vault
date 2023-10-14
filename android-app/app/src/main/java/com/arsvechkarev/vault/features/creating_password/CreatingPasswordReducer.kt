package com.arsvechkarev.vault.features.creating_password

import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.common.domain.CreatingPasswordMode.CreateNew
import com.arsvechkarev.vault.features.common.domain.CreatingPasswordMode.EditExisting
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommand.CheckPasswordStrength
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommand.ComputePasswordCharacteristics
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommand.GeneratePassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommand.GoBack
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommand.SendPasswordChangeEvent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordEvent.ComputedPasswordCharacteristics
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordEvent.GeneratedPassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordEvent.PasswordStrengthChanged
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordNews.SetupExistingPassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordNews.SetupNewPassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordNews.ShowGeneratedPassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnGeneratePasswordClicked
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnPasswordChanged
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnPasswordLengthChanged
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnSavePasswordClicked
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnToggledNumbers
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnToggledSpecialSymbols
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnToggledUppercaseSymbols
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.Setup
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.SetupCompleted
import domain.DEFAULT_PASSWORD_LENGTH
import domain.model.PasswordCharacteristic
import domain.model.PasswordCharacteristic.NUMBERS
import domain.model.PasswordCharacteristic.SPECIAL_SYMBOLS
import domain.model.PasswordCharacteristic.UPPERCASE_SYMBOLS
import java.util.EnumSet

class CreatingPasswordReducer : DslReducer<CreatingPasswordState, CreatingPasswordEvent,
    CreatingPasswordCommand, CreatingPasswordNews>() {
  
  override fun dslReduce(event: CreatingPasswordEvent) {
    when (event) {
      is Setup -> {
        when (event.configuration) {
          CreateNew -> news(SetupNewPassword)
          is EditExisting -> news(SetupExistingPassword(event.configuration.password))
        }
        val length = if (event.configuration is EditExisting) {
          event.configuration.password.stringData.length
        } else {
          DEFAULT_PASSWORD_LENGTH
        }
        state { copy(mode = event.configuration, passwordLength = length) }
      }
      SetupCompleted -> {
        when (val mode = checkNotNull(state.mode)) {
          CreateNew -> {
            val characteristics = EnumSet.allOf(PasswordCharacteristic::class.java)
            commands(GeneratePassword(state.passwordLength, characteristics))
          }
          is EditExisting -> {
            state { copy(password = mode.password) }
            commands(
              CheckPasswordStrength(mode.password),
              ComputePasswordCharacteristics(mode.password),
            )
          }
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
      OnToggledUppercaseSymbols -> {
        state { copy(uppercaseSymbolsEnabled = !state.uppercaseSymbolsEnabled) }
      }
      OnToggledNumbers -> {
        state { copy(numbersEnabled = !state.numbersEnabled) }
      }
      OnToggledSpecialSymbols -> {
        state { copy(specialSymbolsEnabled = !state.specialSymbolsEnabled) }
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
      OnSavePasswordClicked -> {
        commands(SendPasswordChangeEvent(state.password))
      }
      OnBackPressed -> {
        commands(GoBack)
      }
    }
  }
}
