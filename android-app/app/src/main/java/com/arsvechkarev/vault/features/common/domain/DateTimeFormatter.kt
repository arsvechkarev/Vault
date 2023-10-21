package com.arsvechkarev.vault.features.common.domain

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

interface DateTimeFormatter {
  
  fun formatSimple(timestamp: Long): String
  
  fun formatReadable(timestamp: Long): String
}

class SimpleDateTimeFormatter(
  private val timestampProvider: TimestampProvider,
) : DateTimeFormatter {
  
  private val calendar = Calendar.getInstance()
  
  private val simpleFormat = SimpleDateFormat("yyyyMMddHHmmssSS")
  
  // Example:
  // July 4, 12:08, 2001
  private val formatWithYear = SimpleDateFormat("MMMM d, HH:mm, yyyy")
  private val formatWithoutYear = SimpleDateFormat("MMMM d, HH:mm")
  
  override fun formatSimple(timestamp: Long): String {
    return simpleFormat.format(Date(timestamp))
  }
  
  override fun formatReadable(timestamp: Long): String {
    calendar.timeInMillis = timestamp
    val inputYear = calendar.get(Calendar.YEAR)
    calendar.timeInMillis = timestampProvider.now()
    val currentYear = calendar.get(Calendar.YEAR)
    val formatter = if (currentYear == inputYear) formatWithoutYear else formatWithYear
    return formatter.format(timestamp)
  }
}
