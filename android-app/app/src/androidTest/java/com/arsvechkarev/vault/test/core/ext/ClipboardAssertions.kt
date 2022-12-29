package com.arsvechkarev.vault.test.core.ext

import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import androidx.appcompat.app.AppCompatActivity
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import junit.framework.TestCase.assertEquals

fun TestContext<*>.hasClipboardText(text: String) {
  flakySafely {
    val activity = checkNotNull(device.activities.getResumed()) as AppCompatActivity
    val clipboard = activity.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    with(checkNotNull(clipboard.primaryClip)) {
      assertEquals(text, getItemAt(itemCount - 1).text)
    }
  }
}
