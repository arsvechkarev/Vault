package com.arsvechkarev.vault.test.common.base

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

inline fun <reified V : View> BaseAction(
  description: String,
  crossinline action: (UiController, V) -> Unit
) = object : ViewAction {
  
  override fun getConstraints(): Matcher<View> {
    return ViewMatchers.isAssignableFrom(V::class.java)
  }
  
  override fun getDescription(): String = description
  
  override fun perform(uiController: UiController, view: View) {
    action(uiController, view as V)
  }
}
