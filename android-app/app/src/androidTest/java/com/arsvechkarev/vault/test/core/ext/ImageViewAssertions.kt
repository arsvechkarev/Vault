package com.arsvechkarev.vault.test.core.ext

import android.widget.ImageView
import androidx.test.espresso.assertion.ViewAssertions
import com.arsvechkarev.vault.test.core.base.baseMatcher
import com.arsvechkarev.vault.test.core.di.stubs.TestImageRequestsRecorder
import io.github.kakaocup.kakao.image.ImageViewAssertions

fun ImageViewAssertions.wasImageRequestWithUrlCalled(
  url: String,
  recorder: TestImageRequestsRecorder
) {
  view.check(
    ViewAssertions.matches(
      baseMatcher<ImageView>(
        descriptionText = "was performed image request with url $url",
        matcher = { imageView ->
          recorder.getRequestForImage(imageView.id) == url
        }
      )
    )
  )
}
