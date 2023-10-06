package domain.base64

/**
 * Encodes and decodes byte arrays to and from base 64 string
 */
interface Base64Coder {
  
  fun encode(byteArray: ByteArray): String
  
  fun decode(string: String): ByteArray
}