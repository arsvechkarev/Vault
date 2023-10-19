package com.arsvechkarev.vault.features.common.domain

import java.text.SimpleDateFormat
import java.util.Date

interface DateTimeFormatter {
  
  fun format(timestamp: Long): String
}

class SimpleDateTimeFormatter : DateTimeFormatter {
  
  private val format = SimpleDateFormat("yyyyMMddHHmmssSS")
  
  override fun format(timestamp: Long): String {
    return format.format(Date(timestamp))
  }
}
