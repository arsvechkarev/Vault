package com.arsvechkarev.vault.test.common.rule

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.io.File

class ClearPreferencesRule : TestRule {
  
  override fun apply(base: Statement, description: Description): Statement {
    return object : Statement() {
      override fun evaluate() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val absolutePath = context.filesDir.parentFile?.absolutePath
        val sharedPreferencesPath = File(absolutePath + File.separator + "shared_prefs")
        sharedPreferencesPath.listFiles()?.forEach { file ->
          val preferences = context.getSharedPreferences(file.nameWithoutExtension,
            Context.MODE_PRIVATE)
          preferences.edit().clear().commit()
        }
        base.evaluate()
      }
    }
  }
}
