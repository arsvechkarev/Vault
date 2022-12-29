package buisnesslogic

import buisnesslogic.generator.NumbersGenerator
import buisnesslogic.generator.SpecialSymbolsGenerator
import buisnesslogic.generator.UppercaseSymbolsGenerator
import buisnesslogic.model.PasswordCharacteristic
import buisnesslogic.model.PasswordCharacteristic.NUMBERS
import buisnesslogic.model.PasswordCharacteristic.SPECIAL_SYMBOLS
import buisnesslogic.model.PasswordCharacteristic.UPPERCASE_SYMBOLS
import java.util.EnumSet

class PasswordCharacteristicsProviderImpl(
  private val uppercaseSymbolsGenerator: UppercaseSymbolsGenerator,
  private val numbersGenerator: NumbersGenerator,
  private val specialSymbolsGenerator: SpecialSymbolsGenerator,
) : PasswordCharacteristicsProvider {
  
  override fun getCharacteristics(password: String): EnumSet<PasswordCharacteristic> {
    val set = setOfNotNull(
      UPPERCASE_SYMBOLS.takeIf { uppercaseSymbolsGenerator.isPresentInPassword(password) },
      NUMBERS.takeIf { numbersGenerator.isPresentInPassword(password) },
      SPECIAL_SYMBOLS.takeIf { specialSymbolsGenerator.isPresentInPassword(password) }
    )
    return if (set.isNotEmpty()) {
      EnumSet.copyOf(set)
    } else {
      EnumSet.noneOf(PasswordCharacteristic::class.java)
    }
  }
}