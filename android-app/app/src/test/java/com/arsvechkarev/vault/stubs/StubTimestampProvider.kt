package com.arsvechkarev.vault.stubs

import com.arsvechkarev.vault.features.common.domain.TimestampProvider

class StubTimestampProvider : TimestampProvider {
  
  var now = 0L
  
  override fun now() = now
}
