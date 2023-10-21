package com.arsvechkarev.vault.stubs

import com.arsvechkarev.vault.features.common.domain.DateTimeFormatter

class StubDateTimeFormatter : DateTimeFormatter {
  
  override fun formatSimple(timestamp: Long): String {
    return "$timestamp"
  }
  
  override fun formatReadable(timestamp: Long): String {
    return "timestamp=$timestamp"
  }
}
