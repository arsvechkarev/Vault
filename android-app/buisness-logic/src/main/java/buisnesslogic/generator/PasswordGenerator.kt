package buisnesslogic.generator

import buisnesslogic.model.PasswordCharacteristics
import java.util.EnumSet

/**
 * Generates random passwords
 */
interface PasswordGenerator {
  
  /**
   * Generates password of length [length] that has [characteristics]
   *
   * @see PasswordCharacteristics
   */
  fun generatePassword(length: Int, characteristics: EnumSet<PasswordCharacteristics>): String
}