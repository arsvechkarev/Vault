package buisnesslogic.generator

import buisnesslogic.model.PasswordCharacteristics

/**
 * Generates random passwords
 */
interface PasswordGenerator {
  
  /**
   * Generates password of length [length] that has [characteristics]
   *
   * @see PasswordCharacteristics
   */
  fun generatePassword(length: Int, characteristics: Collection<PasswordCharacteristics>): String
}