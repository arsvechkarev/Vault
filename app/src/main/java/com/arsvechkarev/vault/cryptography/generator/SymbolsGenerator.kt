package com.arsvechkarev.vault.cryptography.generator

import com.arsvechkarev.vault.core.SPECIAL_SYMBOLS_STRING
import java.security.SecureRandom

interface SymbolsGenerator {
  
  fun generate(): Char
}

class UppercaseSymbolsGenerator(private val random: SecureRandom) : SymbolsGenerator {
  
  override fun generate(): Char {
    return (random.nextInt(26) + 'A'.toInt()).toChar()
  }
}

class LowercaseSymbolsGenerator(private val random: SecureRandom) : SymbolsGenerator {
  
  override fun generate(): Char {
    return (random.nextInt(26) + 'a'.toInt()).toChar()
  }
}

class NumbersGenerator(private val random: SecureRandom) : SymbolsGenerator {
  
  override fun generate(): Char {
    return (random.nextInt(10) + '0'.toInt()).toChar()
  }
}

class SpecialSymbolsGenerator(private val random: SecureRandom) : SymbolsGenerator {
  
  override fun generate(): Char {
    val randomIndex = random.nextInt(SPECIAL_SYMBOLS_STRING.length)
    return SPECIAL_SYMBOLS_STRING[randomIndex]
  }
}