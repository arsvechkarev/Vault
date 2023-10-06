package domain

import java.util.UUID

interface IdGenerator {
  fun generateRandomId(): UUID
}

object IdGeneratorImpl : IdGenerator {
  override fun generateRandomId(): UUID {
    return UUID.randomUUID()
  }
}
