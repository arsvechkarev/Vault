package com.arsvechkarev.vault.features.common.domain

import java.util.Date

interface TimestampProvider {
  
  fun now(): Long
}

class DefaultTimestampProvider : TimestampProvider {
  
  override fun now(): Long {
    return Date().time
  }
}
