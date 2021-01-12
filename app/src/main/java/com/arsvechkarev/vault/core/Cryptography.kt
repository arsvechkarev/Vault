package com.arsvechkarev.vault.core

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

private const val ALGORITHM = "AES"
private const val SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA256"
private const val CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding"

private val cipher: Cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
private val secretKeyFactory: SecretKeyFactory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM)


fun encryptText(password: String, plaintext: String): String {
  val secretKey = getKeyFromPassword(password, getSaltFromPassword(password))
  val initVector = getIvFromPassword(password)
  cipher.init(Cipher.ENCRYPT_MODE, secretKey, initVector)
  return Base64.encodeToString(cipher.doFinal(plaintext.toByteArray()), Base64.DEFAULT)
}

fun decryptText(password: String, ciphertext: String): String {
  val secretKey = getKeyFromPassword(password, getSaltFromPassword(password))
  val initVector = getIvFromPassword(password)
  cipher.init(Cipher.DECRYPT_MODE, secretKey, initVector)
  return String(cipher.doFinal(Base64.decode(ciphertext, Base64.DEFAULT)))
}

private fun getKeyFromPassword(password: String, salt: String): SecretKey {
  val spec = PBEKeySpec(password.toCharArray(), salt.toByteArray(), 65536, 256)
  return SecretKeySpec(secretKeyFactory.generateSecret(spec).encoded, ALGORITHM)
}

private fun getSaltFromPassword(password: String): String {
  return password.hashCode().toString()
}

private fun getIvFromPassword(password: String): IvParameterSpec {
  val bytes = password.toByteArray()
  val newArray = ByteArray(16)
  for (i in 0..15) {
    if (bytes.size > i) {
      newArray[i] = bytes[i]
    } else {
      newArray[i] = 0
    }
  }
  return IvParameterSpec(newArray)
}