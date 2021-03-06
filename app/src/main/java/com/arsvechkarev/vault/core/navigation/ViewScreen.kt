package com.arsvechkarev.vault.core.navigation

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import com.arsvechkarev.vault.viewdsl.ViewBuilder
import com.arsvechkarev.vault.viewdsl.showKeyboard
import com.arsvechkarev.vault.views.EditTextPassword
import com.arsvechkarev.vault.views.SimpleDialog

abstract class ViewScreen : MvpScreen() {
  
  @PublishedApi
  internal val viewsCache = HashMap<Any, View>()
  internal var _context: Context? = null
  internal var _view: View? = null
  
  val contextNonNull: Context get() = _context!!
  val viewNonNull: View get() = _view!!
  
  val navigator get() = contextNonNull as AppNavigator
  
  abstract fun buildLayout(): View
  
  fun withViewBuilder(builder: ViewBuilder.() -> View): View {
    val viewBuilder = ViewBuilder(contextNonNull)
    return builder(viewBuilder)
  }
  
  fun showKeyboard() {
    contextNonNull.showKeyboard()
  }
  
  fun hideKeyboard() {
    val imm = contextNonNull.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(viewNonNull.windowToken, 0)
  }
  
  fun getString(@StringRes resId: Int, vararg args: Any): CharSequence {
    return contextNonNull.getString(resId, *args)
  }
  
  @Suppress("UNCHECKED_CAST")
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
  
  @Suppress("UNCHECKED_CAST")
  inline fun <reified T : View> viewAs(tag: Any = T::class.java.name): T {
    return view(tag) as T
  }
  
  fun imageView(tag: Any) = viewAs<ImageView>(tag)
  
  fun textView(tag: Any) = viewAs<TextView>(tag)
  
  fun editText(tag: Any) = viewAs<EditText>(tag)
  
  fun editTextPassword(tag: Any) = viewAs<EditTextPassword>(tag)
  
  fun simpleDialog(tag: Any = SimpleDialog::class.java.name) = viewAs<SimpleDialog>(tag)
}