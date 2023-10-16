package com.arsvechkarev.vault.features.common.biometrics

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricConstants.ERROR_CANCELED
import androidx.biometric.BiometricConstants.ERROR_LOCKOUT
import androidx.biometric.BiometricConstants.ERROR_LOCKOUT_PERMANENT
import androidx.biometric.BiometricConstants.ERROR_NEGATIVE_BUTTON
import androidx.biometric.BiometricConstants.ERROR_USER_CANCELED
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.common.biometrics.BiometricsEvent.Error
import com.arsvechkarev.vault.features.common.biometrics.BiometricsEvent.ErrorType.CANCELLED
import com.arsvechkarev.vault.features.common.biometrics.BiometricsEvent.ErrorType.LOCKOUT
import com.arsvechkarev.vault.features.common.biometrics.BiometricsEvent.ErrorType.LOCKOUT_PERMANENT
import com.arsvechkarev.vault.features.common.biometrics.BiometricsEvent.ErrorType.OTHER
import com.arsvechkarev.vault.features.common.biometrics.BiometricsEvent.Success
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import navigation.BaseFragmentScreen
import javax.crypto.Cipher

class BiometricsDialog private constructor(
  screen: BaseFragmentScreen,
  @StringRes titleRes: Int,
) {
  
  private val _events = MutableSharedFlow<BiometricsEvent>(
    extraBufferCapacity = Int.MAX_VALUE,
  )
  
  val events: SharedFlow<BiometricsEvent> get() = _events
  
  private val prompt: BiometricPrompt
  private val promptInfo: BiometricPrompt.PromptInfo
  
  init {
    val activity = screen.requireActivity() as AppCompatActivity
    val executor = ContextCompat.getMainExecutor(activity)
    val callback = object : BiometricPrompt.AuthenticationCallback() {
      
      @SuppressLint("RestrictedApi")
      override fun onAuthenticationError(errCode: Int, errString: CharSequence) {
        when (errCode) {
          ERROR_CANCELED, ERROR_USER_CANCELED, ERROR_NEGATIVE_BUTTON -> {
            _events.tryEmit(Error(CANCELLED))
          }
          ERROR_LOCKOUT -> {
            _events.tryEmit(Error(LOCKOUT))
          }
          ERROR_LOCKOUT_PERMANENT -> {
            _events.tryEmit(Error(LOCKOUT_PERMANENT))
          }
          else -> _events.tryEmit(Error(OTHER))
        }
      }
      
      override fun onAuthenticationFailed() {
      
      }
      
      override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        result.cryptoObject?.cipher?.let {
          _events.tryEmit(Success(BiometricsCryptography.create(it)))
        }
      }
    }
    prompt = BiometricPrompt(activity, executor, callback)
    promptInfo = BiometricPrompt.PromptInfo.Builder().apply {
      setTitle(activity.getText(titleRes))
      setNegativeButtonText(activity.getText(R.string.text_biometrics_cancel))
      setConfirmationRequired(false)
    }.build()
  }
  
  fun launch(cipher: Cipher) {
    prompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
  }
  
  companion object {
    
    fun create(screen: BaseFragmentScreen, titleRes: Int): BiometricsDialog {
      return BiometricsDialog(screen, titleRes)
    }
  }
}
