/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.arsvechkarev.vault.features.common.biometrics

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.arsvechkarev.vault.R
import navigation.BaseFragmentScreen
import javax.crypto.Cipher

object BiometricPromptUtils {
  
  fun createBiometricPrompt(
    screen: BaseFragmentScreen,
    onSuccess: (Cipher) -> Unit,
    onFailed: () -> Unit
  ): BiometricPrompt {
    val activity = screen.requireActivity() as AppCompatActivity
    
    val executor = ContextCompat.getMainExecutor(activity)
    
    val callback = object : BiometricPrompt.AuthenticationCallback() {
      
      override fun onAuthenticationError(errCode: Int, errString: CharSequence) {
        super.onAuthenticationError(errCode, errString)
        onFailed()
      }
      
      override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        onFailed()
      }
      
      override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        result.cryptoObject?.cipher?.let(onSuccess)
      }
    }
    return BiometricPrompt(activity, executor, callback)
  }
  
  fun createPromptInfo(context: Context): BiometricPrompt.PromptInfo {
    return BiometricPrompt.PromptInfo.Builder().apply {
      setTitle(context.getText(R.string.text_biometrics_add_fingerprint))
      setNegativeButtonText(context.getText(R.string.text_biometrics_cancel))
      setConfirmationRequired(false)
    }.build()
  }
}
