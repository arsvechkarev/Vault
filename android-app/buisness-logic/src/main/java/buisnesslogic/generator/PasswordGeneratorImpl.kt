package buisnesslogic.generator

import buisnesslogic.model.PasswordCharacteristic
import buisnesslogic.model.PasswordCharacteristic.NUMBERS
import buisnesslogic.model.PasswordCharacteristic.SPECIAL_SYMBOLS
import buisnesslogic.model.PasswordCharacteristic.UPPERCASE_SYMBOLS
import java.util.EnumSet

// TODO (8/15/2022): Write unit test
class PasswordGeneratorImpl(private val randomGenerator: RandomGenerator) : PasswordGenerator {
  
  private val uppercaseSymbolsGenerator = UppercaseSymbolsGenerator(randomGenerator)
  private val lowercaseSymbolsGenerator = LowercaseSymbolsGenerator(randomGenerator)
  private val numbersGenerator = NumbersGenerator(randomGenerator)
  private val specialSymbolsGenerator = SpecialSymbolsGenerator(randomGenerator)
  
  override fun generatePassword(
    length: Int,
    characteristics: EnumSet<PasswordCharacteristic>
  ): String {
    val generators = getGeneratorsFrom(characteristics)
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
  
  private fun getGeneratorsFrom(
    characteristics: EnumSet<PasswordCharacteristic>
  ): List<SymbolsGenerator> {
    val generators = ArrayList<SymbolsGenerator>()
    generators.add(lowercaseSymbolsGenerator)
    if (characteristics.contains(UPPERCASE_SYMBOLS)) generators.add(uppercaseSymbolsGenerator)
    if (characteristics.contains(NUMBERS)) generators.add(numbersGenerator)
    if (characteristics.contains(SPECIAL_SYMBOLS)) generators.add(specialSymbolsGenerator)
    return generators
  }
}