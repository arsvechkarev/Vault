package com.arsvechkarev.vault.test.core.ext

import androidx.appcompat.app.AppCompatActivity
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import navigation.BaseFragmentScreen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

inline fun <reified T : BaseFragmentScreen> TestContext<*>.currentScreenIs() {
  flakySafely {
    val activity = checkNotNull(device.activities.getResumed()) as AppCompatActivity
    val topFragment = activity.supportFragmentManager.fragments.last()
    assertTrue(topFragment.isVisible)
    assertEquals(T::class.java, topFragment::class.java)
  }
}
