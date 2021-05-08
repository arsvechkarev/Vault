package buisnesslogic.random

import java.nio.ByteBuffer

object SeedRandomGeneratorImpl : SeedRandomGenerator {
  
  override fun generateNumber(seed: ByteArray, limit: Int): Int {
    val buffer = ByteBuffer.wrap(seed)
    buffer.position(15)
    val random = XorWowRandom(buffer.int, buffer.int.shr(31))
    return random.nextInt() % limit
  }
}