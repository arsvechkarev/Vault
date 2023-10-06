package domain

import app.keemobile.kotpass.cryptography.EncryptedValue
import app.keemobile.kotpass.database.Credentials

class Password(val encryptedValueFiled: EncryptedValue) {
  
  val stringData: String get() = encryptedValueFiled.text
  
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    return stringData == (other as Password).stringData
  }
  
  override fun hashCode(): Int {
    return stringData.hashCode()
  }
  
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
  return from(masterPassword.encryptedValueFiled)
}
