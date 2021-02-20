package com.arsvechkarev.vault.cryptography

class EncryptionMetaInfo(
  val salt: ByteArray,
  val encodedSalt: String,
  val iv: ByteArray,
  val unpaddedIv: String
) {
  
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is EncryptionMetaInfo) return false
    
    if (!salt.contentEquals(other.salt)) return false
    if (encodedSalt != other.encodedSalt) return false
    if (!iv.contentEquals(other.iv)) return false
    if (unpaddedIv != other.unpaddedIv) return false
    
    return true
  }
  
  override fun hashCode(): Int {
    var result = salt.contentHashCode()
    result = 31 * result + encodedSalt.hashCode()
    result = 31 * result + iv.contentHashCode()
    result = 31 * result + unpaddedIv.hashCode()
    return result
  }
}