package com.arsvechkarev.vault.test.common.rule

import android.os.IBinder
import android.util.Log
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.lang.reflect.Method
import java.util.Arrays

class DisableAnimationsRule : TestRule {
  
  private lateinit var originalScaleFactors: FloatArray
  
  private var setAnimationScalesMethod: Method? = null
  private var getAnimationScalesMethod: Method? = null
  private var windowManagerObject: Any? = null
  
  init {
    try {
      val windowManagerStubClass = Class.forName("android.view.IWindowManager\$Stub")
      val asInterface = windowManagerStubClass
          .getDeclaredMethod("asInterface", IBinder::class.java)
      val serviceManagerClass = Class.forName("android.os.ServiceManager")
      val getService = serviceManagerClass.getDeclaredMethod("getService", String::class.java)
      val windowManagerClass = Class.forName("android.view.IWindowManager")
      
      // pre-cache the relevant Method objects using reflection, so they're ready to use
      setAnimationScalesMethod = windowManagerClass
          .getDeclaredMethod("setAnimationScales", FloatArray::class.java)
      getAnimationScalesMethod = windowManagerClass.getDeclaredMethod("getAnimationScales")
      val windowManagerBinder = getService.invoke(null, "window") as IBinder
      windowManagerObject = asInterface.invoke(null, windowManagerBinder)
    } catch (e: Exception) {
      throw RuntimeException("Failed to access animation methods", e)
    }
  }
  
  private val animationScaleFactors: FloatArray
    get() = getAnimationScalesMethod?.invoke(windowManagerObject) as FloatArray
  
  
  override fun apply(base: Statement, description: Description): Statement {
    return object : Statement() {
      override fun evaluate() {
        try {
          disableAnimations()
          base.evaluate()
        } finally {
          enableAnimations()
        }
      }
    }
  }
  
  private fun disableAnimations() {
    try {
      originalScaleFactors = animationScaleFactors
      setAnimationScaleFactors()
    } catch (e: Exception) {
      Log.e(TAG, "Failed to disable animations", e)
    }
  }
  
  private fun enableAnimations() {
    try {
      restoreAnimationScaleFactors()
    } catch (e: Exception) {
      Log.e(TAG, "Failed to enable animations", e)
    }
  }
  
  private fun setAnimationScaleFactors() {
    val scaleFactors = FloatArray(originalScaleFactors.size)
    Arrays.fill(scaleFactors, DISABLED)
    setAnimationScalesMethod?.invoke(windowManagerObject, arrayOf<Any>(scaleFactors))
  }
  
  private fun restoreAnimationScaleFactors() {
    setAnimationScalesMethod?.invoke(windowManagerObject, arrayOf<Any>(originalScaleFactors))
  }
  
  companion object {
    
    private val TAG = DisableAnimationsRule::class.java.simpleName
    private const val DISABLED = 0.0f
  }
}
