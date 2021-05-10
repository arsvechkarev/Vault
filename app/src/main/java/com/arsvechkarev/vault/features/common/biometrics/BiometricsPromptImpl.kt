package com.arsvechkarev.vault.features.common.biometrics

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricConstants.ERROR_CANCELED
import androidx.biometric.BiometricConstants.ERROR_LOCKOUT
import androidx.biometric.BiometricConstants.ERROR_LOCKOUT_PERMANENT
import androidx.biometric.BiometricConstants.ERROR_NEGATIVE_BUTTON
import androidx.biometric.BiometricConstants.ERROR_USER_CANCELED
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.common.biometrics.BiometricsPromptEvents.Failure
import com.arsvechkarev.vault.features.common.biometrics.BiometricsPromptEvents.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

@RequiresApi(Build.VERSION_CODES.P)
class BiometricsPromptImpl(
  private val activity: FragmentActivity,
  private val coroutineScope: CoroutineScope
) : BiometricsPrompt {
  
  private val eventsFlow = MutableSharedFlow<BiometricsPromptEvents>()
  
  private val executor = ContextCompat.getMainExecutor(activity)
  
  private val biometricPrompt = BiometricPrompt(activity, executor,
    object : BiometricPrompt.AuthenticationCallback() {
      
      override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        super.onAuthenticationError(errorCode, errString)
        coroutineScope.launch {
          when (errorCode) {
            ERROR_CANCELED, ERROR_USER_CANCELED, ERROR_NEGATIVE_BUTTON -> {
              eventsFlow.emit(BiometricsPromptEvents.Cancelled(errString))
            }
            ERROR_LOCKOUT, ERROR_LOCKOUT_PERMANENT -> {
              eventsFlow.emit(BiometricsPromptEvents.Lockout(errString))
            }
            else -> eventsFlow.emit(BiometricsPromptEvents.Error(errString))
          }
        }
      }
      
      override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        coroutineScope.launch { eventsFlow.emit(Success(result)) }
      }
      
      override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        coroutineScope.launch { eventsFlow.emit(Failure) }
      }
    })
  
  override fun biometricsEvents(): Flow<BiometricsPromptEvents> = eventsFlow
  
  override fun showEncryptingBiometricsPrompt() {
    generateSecretKey(KeyGenParameterSpec.Builder(
      KEY_NAME,
      KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
        .setUserAuthenticationRequired(true)
        .setInvalidatedByBiometricEnrollment(true)
        .build())
    val cipher = getCipher()
    val secretKey = getSecretKey()
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    showPrompt(activity.getText(R.string.text_adding_biometrics_data), cipher)
  }
  
  override fun showDecryptingBiometricsPrompt(iv: ByteArray) {
    val cipher = getCipher()
    val secretKey = getSecretKey()
    cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
    showPrompt(activity.getText(R.string.text_enter_with_fingerprint), cipher)
  }
  
  private fun showPrompt(title: CharSequence, cipher: Cipher) {
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(title)
        .setNegativeButtonText(activity.getText(R.string.text_cancel))
        .build()
    biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
  }
  
  private fun generateSecretKey(keyGenParameterSpec: KeyGenParameterSpec) {
    val keyGenerator = KeyGenerator.getInstance(
      KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
    keyGenerator.init(keyGenParameterSpec)
    keyGenerator.generateKey()
  }
  
  private fun getSecretKey(): SecretKey {
    val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
    keyStore.load(null)
    return keyStore.getKey(KEY_NAME, null) as SecretKey
  }
  
  private fun getCipher(): Cipher {
    return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
        + KeyProperties.BLOCK_MODE_CBC + "/"
        + KeyProperties.ENCRYPTION_PADDING_PKCS7)
  }
  
  private companion object {
    
    const val KEY_NAME = "KeyForPassword"
    const val ANDROID_KEY_STORE = "AndroidKeyStore"
  }
}