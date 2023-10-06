package domain.generator

private val LOWERCASE_SYMBOLS_REGEX = Regex("[a-z]")

class LowercaseSymbolsGenerator(private val randomGenerator: RandomGenerator) : SymbolsGenerator {
  
  override fun generate(): Char {
    return (randomGenerator.nextInt(26) + 'a'.code).toChar()
  }
  
  override fun isPresentInPassword(password: String): Boolean {
    return password.contains(LOWERCASE_SYMBOLS_REGEX)
  }
}
