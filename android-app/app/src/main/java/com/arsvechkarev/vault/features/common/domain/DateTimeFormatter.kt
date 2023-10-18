package com.arsvechkarev.vault.features.common.domain

import java.text.SimpleDateFormat
import java.util.Date

interface DateTimeFormatter {
  
  fun format(timestamp: Long): String
}

class SimpleDateTimeFormatter : DateTimeFormatter {
  
  private val format = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SS")
  
  override fun format(timestamp: Long): String {
    return format.format(Date(timestamp))
  }
}
