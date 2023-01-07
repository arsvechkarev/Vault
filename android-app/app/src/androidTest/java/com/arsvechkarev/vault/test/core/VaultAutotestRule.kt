package com.arsvechkarev.vault.test.core

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.arsvechkarev.vault.MainActivity
import com.arsvechkarev.vault.test.core.rule.ClearPreferencesRule
import com.arsvechkarev.vault.test.core.rule.DeleteFilesRule
import com.arsvechkarev.vault.test.core.rule.DisableAnimationsRule
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class VaultAutotestRule(
  private val autoLaunch: Boolean = false
) : TestRule {
  
  private val disableAnimationsRule = DisableAnimationsRule()
  private val deleteFilesRule = DeleteFilesRule()
  private val clearPreferencesRule = ClearPreferencesRule()
  private val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)
  
  fun launchActivity() {
    ActivityScenario.launch(MainActivity::class.java)
  }
  
  override fun apply(base: Statement, description: Description): Statement {
    var chain = RuleChain
        .outerRule(disableAnimationsRule)
        .around(deleteFilesRule)
        .around(clearPreferencesRule)
    if (autoLaunch) {
      chain = chain.around(activityScenarioRule)
    }
    return chain.apply(base, description)
  }
}
