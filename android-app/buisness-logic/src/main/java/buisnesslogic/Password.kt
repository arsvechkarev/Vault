package buisnesslogic

import app.keemobile.kotpass.cryptography.EncryptedValue
import app.keemobile.kotpass.database.Credentials

@JvmInline
value class Password private constructor(internal val passwordData: EncryptedValue) {
  
  val encryptedValue: ByteArray get() = passwordData.getBinary()
  val rawValue: String get() = passwordData.text
  
  companion object {
    
    fun create(plaintext: String): Password {
      return Password(EncryptedValue.fromString(plaintext))
    }
    
    fun fromRaw(byteArray: ByteArray): Password {
      return Password(EncryptedValue.fromBinary(byteArray))
    }
    
    fun empty() = create("")
  }
}


fun Credentials.Companion.from(masterPassword: Password): Credentials {
  return from(masterPassword.passwordData)
}
