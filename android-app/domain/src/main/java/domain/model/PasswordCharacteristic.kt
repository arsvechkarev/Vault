package domain.model

/**
 * Password characteristics i.e. what password has. By default, password consists of only lowercase
 * english letters
 */
enum class PasswordCharacteristic {
  
  /**
   * Password includes uppercase english letters
   */
  UPPERCASE_SYMBOLS,
  
  /**
   * Password includes numbers 0-9
   */
  NUMBERS,
  
  /**
   * Password includes special symbols such as !@&? etc. You could find them all in [SPECIAL_SYMBOLS_STRING]
   */
  SPECIAL_SYMBOLS,
}