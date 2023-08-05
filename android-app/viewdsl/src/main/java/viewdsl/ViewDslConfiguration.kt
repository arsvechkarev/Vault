package viewdsl

import android.app.Application
import android.content.Context
import android.content.res.Resources

object ViewDslConfiguration {
  
  private var _application: Application? = null
  private var _statusBarHeight: Int = 0
  private var _defaultStyles: DefaultStyles = object : DefaultStyles {}
  private var _Animation_durations: AnimationDurations = object : AnimationDurations {}
  
  val DefaultStyles: DefaultStyles get() = _defaultStyles
  val AnimationDurations: AnimationDurations get() = _Animation_durations
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
  
  fun setDefaultStyles(defaultStyles: DefaultStyles) {
    _defaultStyles = defaultStyles
  }
  
  fun setStatusBarHeight(statusBarHeight: Int) {
    _statusBarHeight = statusBarHeight
  }
}
