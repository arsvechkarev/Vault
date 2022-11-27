package com.arsvechkarev.vault.features.common.domain

interface Clipboard {
  
  fun copyToClipboard(labelRes: Int, text: String)
}
