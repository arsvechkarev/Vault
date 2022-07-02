package buisnesslogic.generator

import buisnesslogic.SPECIAL_SYMBOLS_STRING

/**
 * Generates random chars
 */
interface SymbolsGenerator {
  
  fun generate(): Char
}

class UppercaseSymbolsGenerator(private val randomGenerator: RandomGenerator) : SymbolsGenerator {
  
  override fun generate(): Char {
    return (randomGenerator.nextInt(26) + 'A'.code).toChar()
  }
}

class LowercaseSymbolsGenerator(private val randomGenerator: RandomGenerator) : SymbolsGenerator {
  
  override fun generate(): Char {
    return (randomGenerator.nextInt(26) + 'a'.code).toChar()
  }
}

class NumbersGenerator(private val randomGenerator: RandomGenerator) : SymbolsGenerator {
  
  override fun generate(): Char {
    return (randomGenerator.nextInt(10) + '0'.code).toChar()
  }
}

class SpecialSymbolsGenerator(private val randomGenerator: RandomGenerator) : SymbolsGenerator {
  
  override fun generate(): Char {
    val randomIndex = randomGenerator.nextInt(SPECIAL_SYMBOLS_STRING.length)
    return SPECIAL_SYMBOLS_STRING[randomIndex]
  }
}