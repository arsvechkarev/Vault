package com.arsvechkarev.vault.features.common.domain

interface ImageRequestsRecorder {
  
  fun recordUrlRequest(imageId: Int, url: String)
}

object NoOpImageRequestsRecorder : ImageRequestsRecorder {
  
  // This functionality is needed only in tests
  override fun recordUrlRequest(imageId: Int, url: String) = Unit
}
