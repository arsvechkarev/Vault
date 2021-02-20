package com.arsvechkarev.vault.cryptography

import java.nio.ByteBuffer
import kotlin.random.Random

interface SeedRandomGenerator {
  
  fun generateNumber(seed: ByteArray, limit: Int): Int
}

object SeedRandomGeneratorImpl : SeedRandomGenerator {
  
  override fun generateNumber(seed: ByteArray, limit: Int): Int {
    val buffer = ByteBuffer.wrap(seed)
    buffer.position(15)
    val random = Random(buffer.int)
    return random.nextInt(limit)
  }
}
