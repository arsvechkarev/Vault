package domain.generator

/**
 * Generates random chars
 */
interface SymbolsGenerator {
  
  fun generate(): Char
  
  /** See [PasswordGeneratorImpl.tryGeneratePassword] */
  fun isPresentInPassword(password: String): Boolean
}
