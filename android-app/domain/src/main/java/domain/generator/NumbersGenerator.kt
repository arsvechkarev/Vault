package domain.generator

private val NUMBERS_REGEX = Regex("\\d")

class NumbersGenerator(private val randomGenerator: RandomGenerator) : SymbolsGenerator {
  
  override fun generate(): Char {
    return (randomGenerator.nextInt(10) + '0'.code).toChar()
  }
  
  override fun isPresentInPassword(password: String): Boolean {
    return password.contains(NUMBERS_REGEX)
  }
}
