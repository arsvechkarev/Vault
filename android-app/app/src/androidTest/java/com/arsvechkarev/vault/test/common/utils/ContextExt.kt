package com.arsvechkarev.vault.test.common.utils

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry

val context: Context get() = InstrumentationRegistry.getInstrumentation().context

val targetContext: Context get() = InstrumentationRegistry.getInstrumentation().targetContext
