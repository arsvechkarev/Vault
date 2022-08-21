package buisnesslogic.generator

import buisnesslogic.model.PasswordCharacteristic
import java.util.EnumSet

/**
 * Generates random passwords
 */
interface PasswordGenerator {
  
  /**
   * Generates password of length [length] that has [characteristics]
   *
   * @see PasswordCharacteristic
   */
  fun generatePassword(length: Int, characteristics: EnumSet<PasswordCharacteristic>): String
}