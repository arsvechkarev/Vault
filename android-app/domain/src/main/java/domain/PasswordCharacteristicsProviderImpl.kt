package domain

import domain.generator.NumbersGenerator
import domain.generator.SpecialSymbolsGenerator
import domain.generator.UppercaseSymbolsGenerator
import domain.model.PasswordCharacteristic
import domain.model.PasswordCharacteristic.NUMBERS
import domain.model.PasswordCharacteristic.SPECIAL_SYMBOLS
import domain.model.PasswordCharacteristic.UPPERCASE_SYMBOLS
import java.util.EnumSet

class PasswordCharacteristicsProviderImpl(
  private val uppercaseSymbolsGenerator: UppercaseSymbolsGenerator,
  private val numbersGenerator: NumbersGenerator,
  private val specialSymbolsGenerator: SpecialSymbolsGenerator,
) : PasswordCharacteristicsProvider {
  
  override fun getCharacteristics(password: Password): EnumSet<PasswordCharacteristic> {
    val passwordString = password.stringData
    val set = setOfNotNull(
      UPPERCASE_SYMBOLS.takeIf { uppercaseSymbolsGenerator.isPresentInPassword(passwordString) },
      NUMBERS.takeIf { numbersGenerator.isPresentInPassword(passwordString) },
      SPECIAL_SYMBOLS.takeIf { specialSymbolsGenerator.isPresentInPassword(passwordString) }
    )
    return if (set.isNotEmpty()) {
      EnumSet.copyOf(set)
    } else {
      EnumSet.noneOf(PasswordCharacteristic::class.java)
    }
  }
}