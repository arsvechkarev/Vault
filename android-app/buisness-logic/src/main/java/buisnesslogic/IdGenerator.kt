package buisnesslogic

import java.util.UUID

interface IdGenerator {
  fun generateRandomId(): String
}

object IdGeneratorImpl : IdGenerator {
  override fun generateRandomId(): String {
    return UUID.randomUUID().toString()
  }
}
