package domain.generator

private val UPPERCASE_SYMBOLS_REGEX = Regex("[A-Z]")

class UppercaseSymbolsGenerator(private val randomGenerator: RandomGenerator) : SymbolsGenerator {
  
  override fun generate(): Char {
    return (randomGenerator.nextInt(26) + 'A'.code).toChar()
  }
  
  override fun isPresentInPassword(password: String): Boolean {
    return password.contains(UPPERCASE_SYMBOLS_REGEX)
  }
}
