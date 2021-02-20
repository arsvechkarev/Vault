package com.arsvechkarev.vault.cryptography

interface Base64Coder {
  
  fun encode(byteArray: ByteArray): String
  
  fun decode(string: String): ByteArray
}