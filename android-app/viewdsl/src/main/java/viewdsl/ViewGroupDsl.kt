package viewdsl

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.addViews(vararg views: View) {
  views.forEach { addView(it) }
}

fun ViewGroup.addView(block: () -> View) {
  addView(block())
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

inline fun ViewGroup.forEachChild(action: (child: View) -> Unit) {
  for (i in 0 until childCount) action(getChildAt(i))
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
  return LayoutInflater.from(context).inflate(layoutRes, this, false)
}