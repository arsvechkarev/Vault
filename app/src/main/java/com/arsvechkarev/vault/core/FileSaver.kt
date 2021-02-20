package com.arsvechkarev.vault.core

interface FileSaver {
  
  fun saveTextToFile(filename: String, text: String)
  
  fun readTextFromFile(filename: String): String
}