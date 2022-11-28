package viewdsl

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.addView(block: () -> View): View {
  val view = block()
  addView(view)
  return view
}

val ViewGroup.children: Iterable<View>
  get() = object : Iterable<View> {
    override fun iterator(): Iterator<View> {
      return object : Iterator<View> {
        
        private var currentIndex = 0
        
        override fun hasNext(): Boolean {
          return currentIndex < childCount
        }
        
        override fun next(): View {
          val child = getChildAt(currentIndex)
          currentIndex++
          return child
        }
      }
    }
  }

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
  return LayoutInflater.from(context).inflate(layoutRes, this, false)
}
