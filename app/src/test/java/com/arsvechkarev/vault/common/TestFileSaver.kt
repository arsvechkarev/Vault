package com.arsvechkarev.vault.common

import com.arsvechkarev.vault.core.FileSaver

class TestFileSaver : FileSaver {
  
  private var text: String = ""
  
  override fun saveTextToFile(filename: String, text: String) {
    this.text = text
  }
  
  override fun readTextFromFile(filename: String): String {
    return text
  }
}