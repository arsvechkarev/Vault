package com.arsvechkarev.vault.common

import buisnesslogic.FileSaver

class TestFileSaver : FileSaver {
  
  private var text: String = ""
  
  override fun saveTextToFile(text: String) {
    this.text = text
  }
  
  override fun readTextFromFile(): String {
    return text
  }
  
  override fun deleteFile() {
    text = ""
  }
}