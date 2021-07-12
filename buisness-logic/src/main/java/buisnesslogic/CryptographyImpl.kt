package buisnesslogic

import buisnesslogic.base64.Base64Coder
import buisnesslogic.random.SeedRandomGenerator
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/**
 * Class that performs actual encryption/decryption of data with master password
 */
class CryptographyImpl(
  private val base64Coder: Base64Coder,
  private val seedRandomGenerator: SeedRandomGenerator
) : Cryptography {
  
  /**
   * Performs initial encryption of [data] with [password] and returns encrypted text. After that
   * returns result can be decrypted with the [password] and [data] can be retrieved
   */
  override fun encryptForTheFirstTime(password: String, data: String): String {
    val saltSize = getSaltBytesSize(password)
    val initialSalt = generateRandomArray(MAX_SALT_SIZE)
    val salt = ByteArray(saltSize)
    val iv = generateRandomArray(IV_SIZE)
    System.arraycopy(initialSalt, 0, salt, 0, saltSize)
    val encodedSalt = base64Coder.encode(salt)
    val encode1 = base64Coder.encode(iv)
    val encodedIv = encode1.dropLast(2)
    val spec = PBEKeySpec(password.toCharArray(), salt, SECRET_KEY_ITERATIONS, SECRET_KEY_LENGTH)
    val factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM)
    val secretKey = SecretKeySpec(factory.generateSecret(spec).encoded, ALGORITHM)
    val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(iv))
    val encode = base64Coder.encode(cipher.doFinal(data.toByteArray(charset)))
    return encodedSalt + encodedIv + encode
  }
  
  /**
   * Encrypts [plaintext] with [password] and returns encrypted data. In addition to [plaintext]
   * and [password] you should provide [ciphertext] that contains meta information about encryption,
   * such as salt, iv, etc. In order to get ciphertext, you should call [encryptForTheFirstTime] first
   *
   * @see encryptForTheFirstTime
   * @see decryptData
   */
  override fun encryptData(password: String, plaintext: String, ciphertext: String): String {
    val metaInfo = getEncryptionMetaInfo(password, ciphertext)
    val spec = PBEKeySpec(password.toCharArray(), metaInfo.salt,
      SECRET_KEY_ITERATIONS, SECRET_KEY_LENGTH)
    val factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM)
    val secretKey = SecretKeySpec(factory.generateSecret(spec).encoded, ALGORITHM)
    val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(metaInfo.iv))
    val encode = base64Coder.encode(cipher.doFinal(plaintext.toByteArray(charset)))
    return metaInfo.encodedSalt + metaInfo.unpaddedIv + encode
  }
  
  /**
   * Decrypts [ciphertext] using given [password] and returns decrypted plaintext. If decryption
   * was unsuccessful, throws exception
   */
  override fun decryptData(password: String, ciphertext: String): String {
    val metaInfo = getEncryptionMetaInfo(password, ciphertext)
    val saltLength = metaInfo.encodedSalt.length
    val encryptedData = ciphertext.substring(saltLength + 22, ciphertext.length)
    val spec = PBEKeySpec(password.toCharArray(), metaInfo.salt,
      SECRET_KEY_ITERATIONS, SECRET_KEY_LENGTH)
    val factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM)
    val secretKey = SecretKeySpec(factory.generateSecret(spec).encoded, ALGORITHM)
    val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
    cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(metaInfo.iv))
    return String(cipher.doFinal(base64Coder.decode(encryptedData)), charset)
  }
  
  private fun getEncryptionMetaInfo(password: String, ciphertext: String): EncryptionMetaInfo {
    val saltBytesSize = getSaltBytesSize(password)
    val saltLength = getSaltStringLength(saltBytesSize)
    val encodedSalt = ciphertext.take(saltLength)
    val encodedIv = ciphertext.substring(saltLength, saltLength + 22) + "=="
    val salt = base64Coder.decode(encodedSalt)
    val iv = base64Coder.decode(encodedIv)
    return EncryptionMetaInfo(salt, encodedSalt, iv, encodedIv.dropLast(2))
  }
  
  private fun getSaltStringLength(saltBytesSize: Int): Int {
    return saltBytesSize / 3 * 4
  }
  
  private fun getSaltBytesSize(password: String): Int {
    val sha256 = MessageDigest.getInstance(SHA_256)
    val digest = sha256.digest(password.toByteArray(charset))
    sha256.reset()
    val randomNumber = seedRandomGenerator.generateNumber(digest, MAX_SALT_SIZE / 2)
    val size = MAX_SALT_SIZE / 2 + randomNumber
    return size - (size % 12)
  }
  
  private fun generateRandomArray(size: Int): ByteArray {
    val random = SecureRandom()
    val byteArray = ByteArray(size)
    random.nextBytes(byteArray)
    return byteArray
  }
  
  /**
   * Meta information about encryption stuff
   */
  class EncryptionMetaInfo(
    val salt: ByteArray,
    val encodedSalt: String,
    val iv: ByteArray,
    val unpaddedIv: String
  )
  
  companion object {
    
    private val charset = Charsets.UTF_8
    
    private const val ALGORITHM = "AES"
    private const val SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA256"
    private const val CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding"
    private const val SECRET_KEY_LENGTH = 256
    private const val SECRET_KEY_ITERATIONS = 75288
    private const val SHA_256 = "SHA-256"
    private const val MAX_SALT_SIZE = 200
    private const val IV_SIZE = 16
  }
}