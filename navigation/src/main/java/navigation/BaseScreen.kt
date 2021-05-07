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
  
  fun view(tag: Any): View {
    if (viewsCache[tag] == null) {
      if (tag is Int) {
        viewsCache[tag] = viewNonNull.findViewById(tag)
      } else {
        viewsCache[tag] = viewNonNull.findViewWithTag(tag)
      }
    }
    return viewsCache.getValue(tag)
  }
  
  inline fun <reified T : View> viewAs(tag: Any = T::class.java.name): T {
    return view(tag) as T
  }
  
  fun imageView(tag: Any) = viewAs<ImageView>(tag)
  
  fun textView(tag: Any) = viewAs<TextView>(tag)
  
  fun editText(tag: Any) = viewAs<EditText>(tag)
}