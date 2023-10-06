package domain.base64

object JavaBase64Coder : Base64Coder {
  
  override fun encode(byteArray: ByteArray): String {
    return Base64.getEncoder().encodeToString(byteArray)
  }
  
  override fun decode(string: String): ByteArray {
    return Base64.getDecoder().decode(string)
  }
}