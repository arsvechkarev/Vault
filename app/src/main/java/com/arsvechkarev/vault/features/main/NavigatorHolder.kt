package com.arsvechkarev.vault.features.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.arsvechkarev.vault.core.navigation.Navigator
import com.arsvechkarev.vault.core.navigation.NavigatorImpl
import com.arsvechkarev.vault.core.navigation.Screen
import com.arsvechkarev.vault.core.navigation.ScreenHandler
import com.arsvechkarev.vault.core.navigation.ScreenHandlerFactory
import com.arsvechkarev.vault.core.navigation.ViewNavigationHost
import com.arsvechkarev.vault.core.navigation.ViewScreen
import com.arsvechkarev.vault.core.navigation.ViewScreenHandler

// Releasing context in release(), so should be no leaks
@SuppressLint("StaticFieldLeak")
object NavigatorHolder {
  
  private var _navigator: Navigator? = null
  private var _context: Context? = null
  
  private val factory = object : ScreenHandlerFactory {
    override fun createScreenHandler(screen: Screen): ScreenHandler {
      return ViewScreenHandler(screen as ViewScreen, _context!!)
    }
  }
  
  private val converter: (ScreenHandler) -> View = { handler ->
    (handler as ViewScreenHandler).view
  }
  
  val navigator: Navigator get() = _navigator!!
  
  fun initNavigator(root: ViewGroup) {
    _context = root.context
    val host = ViewNavigationHost(root, converter)
    _navigator = NavigatorImpl(host, factory)
  }
  
  fun release() {
    _context = null
    _navigator = null
  }
}