package com.arsvechkarev.vault.test.core.rule

import androidx.test.espresso.intent.Intents
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class InitIntentsRule : TestRule {
  
  override fun apply(base: Statement, description: Description): Statement {
    return object : Statement() {
      override fun evaluate() {
        Intents.init()
        base.evaluate()
      }
    }
  }
}