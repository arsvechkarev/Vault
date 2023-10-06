package domain.generator

import java.security.SecureRandom

interface RandomGenerator {
  
  fun nextInt(bound: Int): Int
}

object SecureRandomGenerator : RandomGenerator {
  
  private val secureRandom = SecureRandom()
  
  override fun nextInt(bound: Int): Int {
    return secureRandom.nextInt(bound)
  }
}
