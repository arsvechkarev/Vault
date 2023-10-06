package domain.generator

private const val SPECIAL_SYMBOLS_STRING = "!@#\$%^&*\"()_\\-=+/.><?,';"

class SpecialSymbolsGenerator(private val randomGenerator: RandomGenerator) : SymbolsGenerator {
  
  override fun generate(): Char {
    val randomIndex = randomGenerator.nextInt(SPECIAL_SYMBOLS_STRING.length)
    return SPECIAL_SYMBOLS_STRING[randomIndex]
  }
  
  override fun isPresentInPassword(password: String): Boolean {
    for (char in SPECIAL_SYMBOLS_STRING) {
      if (password.contains(char)) return true
    }
    return false
  }
}