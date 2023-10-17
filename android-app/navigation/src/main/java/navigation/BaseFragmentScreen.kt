package navigation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

abstract class BaseFragmentScreen : Fragment() {
  
  internal var screenKey: ScreenKey? = null
  
  private val viewsCache = HashMap<Any, View>()
  
  abstract fun buildLayout(context: Context): View
  
  open fun handleBackPress(): Boolean = false
  
  open fun onInit() = Unit
  
  open fun onViewCreated() = Unit
  
  open fun onAppearedOnScreen() = Unit
  
  open fun onDisappearedFromScreen() = Unit
  
  open fun onRelease() = Unit
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    onInit()
  }
  
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return buildLayout(requireContext())
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    onViewCreated()
  }
  
  override fun onResume() {
    onAppearedOnScreen()
    super.onResume()
  }
  
  override fun onStop() {
    super.onStop()
    onDisappearedFromScreen()
  }
  
  override fun onDestroy() {
    super.onDestroy()
    onRelease()
  }
  
  fun view(tagOrId: Any): View {
    return checkNotNull(viewNullable(tagOrId))
  }
  
  fun viewNullable(tagOrId: Any): View? {
    if (viewsCache[tagOrId] == null) {
      if (tagOrId is Int) {
        viewsCache[tagOrId] = requireView().findViewById(tagOrId)
      } else {
        viewsCache[tagOrId] = requireView().findViewWithTag(tagOrId)
      }
    }
    return viewsCache[tagOrId]
  }
  
  inline fun <reified T : View> viewAs(tagOrId: Any = T::class.java.name): T {
    return view(tagOrId) as T
  }
  
  inline fun <reified T : View> viewAsNullable(tagOrId: Any = T::class.java.name): T? {
    return viewNullable(tagOrId) as? T?
  }
  
  fun imageView(tagOrId: Any) = viewAs<ImageView>(tagOrId)
  
  fun textView(tagOrId: Any) = viewAs<TextView>(tagOrId)
  
  fun editText(tagOrId: Any) = viewAs<EditText>(tagOrId)
}
