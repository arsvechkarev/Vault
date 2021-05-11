package navigation

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import moxy.MvpDelegate
import moxy.MvpDelegateHolder

abstract class BaseScreen : Screen, MvpDelegateHolder {
  
  private val _mvpDelegate: MvpDelegate<out Screen> by lazy { MvpDelegate(this) }
  private val viewsCache = HashMap<Any, View>()
  
  internal var _arguments: Bundle = Bundle.EMPTY
  internal var _view: View? = null
  
  override fun getMvpDelegate() = _mvpDelegate
  
  val viewNonNull: View get() = _view!!
  
  val contextNonNull: Context get() = viewNonNull.context!!
  
  override val arguments: Bundle
    get() = _arguments
  
  abstract fun buildLayout(context: Context): View
  
  internal fun setParentDelegate(parentDelegate: MvpDelegate<*>, screenKey: String) {
    mvpDelegate.setParentDelegate(parentDelegate, screenKey)
  }
  
  internal fun clearViewCache() {
    viewsCache.clear()
  }
  
  fun getText(@StringRes resId: Int, vararg args: Any): CharSequence {
    return contextNonNull.getString(resId, *args)
  }
  
  fun view(tagOrId: Any): View {
    if (viewsCache[tagOrId] == null) {
      if (tagOrId is Int) {
        viewsCache[tagOrId] = viewNonNull.findViewById(tagOrId)
      } else {
        viewsCache[tagOrId] = viewNonNull.findViewWithTag(tagOrId)
      }
    }
    return viewsCache.getValue(tagOrId)
  }
  
  inline fun <reified T : View> viewAs(tagOrId: Any = T::class.java.name): T {
    return view(tagOrId) as T
  }
  
  fun imageView(tagOrId: Any) = viewAs<ImageView>(tagOrId)
  
  fun textView(tagOrId: Any) = viewAs<TextView>(tagOrId)
  
  fun editText(tagOrId: Any) = viewAs<EditText>(tagOrId)
}