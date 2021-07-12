package buisnesslogic.random

object SeedRandomGeneratorImpl : SeedRandomGenerator {
  
  override fun generateNumber(seed: ByteArray, limit: Int): Int {
    val value = seed[15]
    val random = XorWowRandom(value.toInt(), value.toInt().shr(31))
    return random.nextInt() % limit
  }
}