package com.arsvechkarev.vault.test.core.rule

import android.provider.Settings.Global.ANIMATOR_DURATION_SCALE
import android.provider.Settings.Global.TRANSITION_ANIMATION_SCALE
import android.provider.Settings.Global.WINDOW_ANIMATION_SCALE
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class DisableAnimationsRule : TestRule {
  
  override fun apply(base: Statement, description: Description): Statement {
    return object : Statement() {
      override fun evaluate() {
        try {
          setAnimations(false)
          base.evaluate()
        } finally {
          setAnimations(true)
        }
      }
    }
  }
  
  private fun setAnimations(enabled: Boolean) {
    val value = if (enabled) "1.0" else "0.0"
    InstrumentationRegistry.getInstrumentation().uiAutomation.run {
      executeShellCommand("settings put global $WINDOW_ANIMATION_SCALE $value")
      executeShellCommand("settings put global $TRANSITION_ANIMATION_SCALE $value")
      executeShellCommand("settings put global $ANIMATOR_DURATION_SCALE $value")
    }
  }
}
