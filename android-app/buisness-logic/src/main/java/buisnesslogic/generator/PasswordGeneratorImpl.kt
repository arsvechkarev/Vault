package buisnesslogic.generator

import buisnesslogic.model.PasswordCharacteristic
import buisnesslogic.model.PasswordCharacteristic.NUMBERS
import buisnesslogic.model.PasswordCharacteristic.SPECIAL_SYMBOLS
import buisnesslogic.model.PasswordCharacteristic.UPPERCASE_SYMBOLS
import java.util.EnumSet

class PasswordGeneratorImpl(private val randomGenerator: RandomGenerator) : PasswordGenerator {
  
  override fun generatePassword(
    length: Int,
    characteristics: EnumSet<PasswordCharacteristic>
  ): String {
    val generators = createSymbolsGeneratorsList(randomGenerator, characteristics)
    while (true) {
      val password = tryGeneratePassword(length, generators)
      if (hasAllCharacteristics(password, generators)) {
        return password
      }
    }
  }
  
  // Sometimes generated password does not have all required characteristics
  // because generators are chosen randomly and there is no guarantee that each
  // generator is invoked at least once. That is why we have to keep generating
  // passwords until we get one that has all required characteristics
  private fun hasAllCharacteristics(
    password: String,
    generators: List<SymbolsGenerator>
  ): Boolean {
    return generators.all { it.isPresentInPassword(password) }
  }
  
  private fun tryGeneratePassword(length: Int, generators: List<SymbolsGenerator>): String {
    val array = CharArray(length)
    repeat(length) { i ->
      val randomGenerator = generators[randomGenerator.nextInt(generators.size)]
      array[i] = randomGenerator.generate()
    }
    return String(array)
  }
  
  companion object {
    
    fun createSymbolsGeneratorsList(
      randomGenerator: RandomGenerator,
      characteristics: EnumSet<PasswordCharacteristic>,
    ): List<SymbolsGenerator> = listOfNotNull(
      LowercaseSymbolsGenerator(randomGenerator),
      UppercaseSymbolsGenerator(randomGenerator).takeIf {
        characteristics.contains(UPPERCASE_SYMBOLS)
      },
      NumbersGenerator(randomGenerator).takeIf {
        characteristics.contains(NUMBERS)
      },
      SpecialSymbolsGenerator(randomGenerator).takeIf {
        characteristics.contains(SPECIAL_SYMBOLS)
      },
    )
  }
}
