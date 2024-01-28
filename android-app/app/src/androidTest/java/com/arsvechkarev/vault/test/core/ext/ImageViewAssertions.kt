package com.arsvechkarev.vault.test.core.ext

import android.widget.ImageView
import androidx.test.espresso.assertion.ViewAssertions
import com.arsvechkarev.vault.features.common.domain.ImageRequestsRecorder.Companion.EMPTY_RECORD_STUB
import com.arsvechkarev.vault.test.core.base.baseMatcher
import com.arsvechkarev.vault.test.core.di.stubs.TestImageRequestsRecorder
import io.github.kakaocup.kakao.common.matchers.DrawableMatcher
import io.github.kakaocup.kakao.image.ImageViewAssertions

fun ImageViewAssertions.hasNoDrawable() {
  view.check(ViewAssertions.matches(DrawableMatcher(drawable = null)))
}

fun ImageViewAssertions.wasImageRequestWithUrlPerformed(
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

fun ImageViewAssertions.wasNoImageRequestPerformed(
  recorder: TestImageRequestsRecorder
) {
  view.check(
    ViewAssertions.matches(
      baseMatcher<ImageView>(
        descriptionText = "was not performed any image request",
        matcher = { imageView ->
          recorder.getRequestForImage(imageView.id) == EMPTY_RECORD_STUB
        }
      )
    )
  )
}
