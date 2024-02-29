package domain

import app.keemobile.kotpass.cryptography.EncryptedValue
import app.keemobile.kotpass.database.Credentials

class Password(val encryptedValueField: EncryptedValue) {
  
  val stringData: String get() = encryptedValueField.text
  
  val isNotEmpty get() = encryptedValueField.byteLength != 0
  
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


fun Credentials.Companion.from(masterPassword: Password, keyData: ByteArray? = null): Credentials {
  if (keyData != null) {
    return from(masterPassword.encryptedValueField, keyData)
  }
  return from(masterPassword.encryptedValueField)
}
