package domain

import java.time.Instant

interface InstantProvider {
  fun now(): Instant
}

object RealInstantProvider : InstantProvider {
  
  override fun now(): Instant {
    return Instant.now()
  }
}
