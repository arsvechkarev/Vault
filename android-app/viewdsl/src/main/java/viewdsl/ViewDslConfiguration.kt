package viewdsl

import android.app.Application
import android.content.Context
import android.content.res.Resources

object ViewDslConfiguration {
  
  private var _application: Application? = null
  private var _statusBarHeight: Int = 0
  private var _coreStyles: CoreStyles? = null
  
  val CoreStyles: CoreStyles get() = checkNotNull(_coreStyles)
  
  val statusBarHeight: Int get() = _statusBarHeight
  
  val applicationContext: Context
    get() = checkNotNull(_application?.applicationContext) {
      "You should call 'initializeWithAppContext' before accessing applicationContext"
    }
  
  fun initializeWithAppContext(application: Application) {
    _application = application
  }
  
  fun initializeResources(resources: Resources) {
    Densities.init(resources)
  }
  
  fun setCoreStyles(coreStyles: CoreStyles) {
    _coreStyles = coreStyles
  }
  
  fun setStatusBarHeight(statusBarHeight: Int) {
    _statusBarHeight = statusBarHeight
  }
}
