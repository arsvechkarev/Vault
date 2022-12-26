package com.arsvechkarev.vault.test.core.rule

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class DeleteFilesRule : TestRule {
  
  override fun apply(base: Statement, description: Description): Statement {
    return object : Statement() {
      override fun evaluate() {
        InstrumentationRegistry.getInstrumentation().targetContext.apply {
          filesDir.deleteRecursively()
          cacheDir.deleteRecursively()
        }
        base.evaluate()
      }
    }
  }
}
