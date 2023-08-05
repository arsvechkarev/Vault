package viewdsl

import android.content.res.Resources
import android.util.DisplayMetrics

object Densities {
  
  private var _density: Float = 1f
  private var _scaledDensity: Float = 1f
  private var _densityDpi: Int = DisplayMetrics.DENSITY_DEFAULT
  
  val density: Float get() = _density
  val scaledDensity: Float get() = _scaledDensity
  val densityDpi: Int get() = _densityDpi
  
  internal fun init(resources: Resources) {
    with(resources.displayMetrics) {
      _density = density
      _scaledDensity = scaledDensity
      _densityDpi = densityDpi
    }
  }
}
