package com.arsvechkarev.vault.test.core.base

import android.view.View
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description

inline fun <reified V : View> baseMatcher(
  descriptionText: String,
  crossinline matcher: (V) -> Boolean,
) = object : BoundedMatcher<View, V>(V::class.java) {
  
  override fun describeTo(description: Description) {
    description.appendText(descriptionText)
  }
  
  override fun matchesSafely(item: V): Boolean {
    return matcher(item)
  }
}
