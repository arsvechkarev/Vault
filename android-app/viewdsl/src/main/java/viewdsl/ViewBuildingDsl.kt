@file:Suppress("unused")

package viewdsl

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.Size.IntSize
import android.view.ViewGroup.LayoutParams as ViewGroupLayoutParams
import android.widget.FrameLayout.LayoutParams as FrameLayoutParams
import android.widget.LinearLayout.LayoutParams as LinearLayoutParams
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams as ConstraintLayoutParams
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams as CoordLayoutParams

inline fun <reified T : View> Context.withViewBuilder(builder: ViewBuilder.() -> T): T {
  val viewBuilder = ViewBuilder(this)
  return builder(viewBuilder)
}

inline fun View.withViewBuilder(builder: ViewBuilder.() -> Unit) {
  val viewBuilder = ViewBuilder(context)
  builder(viewBuilder)
}

class ViewBuilder(val context: Context) {
  
  val StatusBarHeight get() = context.statusBarHeight
  
  private inline val defaultRootStyle get() = ViewDslConfiguration.CoreStyles.BaseRootBackground
  
  fun Any.RootVerticalLayout(
    width: Size = MatchParent,
    height: Size = MatchParent,
    style: Style<LinearLayout> = {},
    block: Style<LinearLayout> = {}
  ) = LinearLayout(context).size(width, height).apply(style).apply(block).apply {
    orientation(LinearLayout.VERTICAL)
  }
  
  fun Any.RootHorizontalLayout(
    width: Size = MatchParent,
    height: Size = MatchParent,
    style: Style<LinearLayout> = {},
    block: Style<LinearLayout> = {}
  ) = LinearLayout(context).size(width, height).apply(style).apply(block).apply {
    orientation(LinearLayout.HORIZONTAL)
  }
  
  fun Any.RootScrollableVerticalLayout(
    width: Size = MatchParent,
    height: Size = MatchParent,
    style: Style<LinearLayout> = {},
    block: Style<LinearLayout> = {}
  ) = ScrollView(context).size(width, height).apply {
    val compositeStyle = defaultRootStyle thenApply style
    VerticalLayout(MatchParent, WrapContent, compositeStyle, block)
  }
  
  fun Any.RootScrollableConstraintLayout(
    width: Size = MatchParent,
    height: Size = MatchParent,
    style: Style<ConstraintLayout> = {},
    block: Style<ConstraintLayout> = {}
  ) = ScrollView(context).size(width, height).apply {
    val compositeStyle = defaultRootStyle thenApply style
    child(MatchParent, MatchParent, compositeStyle, block)
  }
  
  fun Any.RootFrameLayout(
    width: Size = MatchParent,
    height: Size = MatchParent,
    style: Style<FrameLayout> = {},
    block: Style<FrameLayout> = {}
  ) = FrameLayout(context).size(width, height).apply(defaultRootStyle).apply(style).apply(block)
  
  fun Any.RootCoordinatorLayout(
    width: Size = MatchParent,
    height: Size = MatchParent,
    style: CoordinatorLayout.() -> Unit = {},
    block: CoordinatorLayout.() -> Unit = {}
  ) = CoordinatorLayout(context).size(width, height).apply(defaultRootStyle).apply(style)
      .apply(block)
  
  fun Any.RootConstraintLayout(
    width: Size = MatchParent,
    height: Size = MatchParent,
    style: Style<ConstraintLayout> = {},
    block: Style<ConstraintLayout> = {}
  ) = ConstraintLayout(context).size(width, height).apply(defaultRootStyle).apply(style)
      .apply(block)
  
  fun Any.RootTextView(
    width: Size,
    height: Size,
    style: Style<TextView> = {},
    block: Style<TextView> = {}
  ) = TextView(context).size(width, height).apply(style).apply(block)
  
  inline fun <reified T : View> FrameLayout.child(
    width: Size, height: Size, style: T.() -> Unit = {}, block: T.() -> Unit = {},
  ) = child<T, FrameLayoutParams>(width, height, style, block)
  
  inline fun <reified T : View> FrameLayout.child(
    width: Int, height: Int, style: T.() -> Unit = {}, block: T.() -> Unit = {}
  ) = child<T, FrameLayoutParams>(IntSize(width), IntSize(height), style, block)
  
  inline fun <reified T : View> LinearLayout.child(
    width: Size, height: Size, style: T.() -> Unit = {}, block: T.() -> Unit = {}
  ) = child<T, LinearLayoutParams>(width, height, style, block)
  
  inline fun <reified T : View> LinearLayout.child(
    width: Int, height: Int, style: T.() -> Unit = {}, block: T.() -> Unit = {}
  ) = child<T, LinearLayoutParams>(IntSize(width), IntSize(height), style, block)
  
  inline fun <reified T : View> CoordinatorLayout.child(
    width: Size, height: Size, style: T.() -> Unit = {}, block: T.() -> Unit = {},
  ) = child<T, CoordLayoutParams>(width, height, style, block)
  
  inline fun <reified T : View> CoordinatorLayout.child(
    width: Int, height: Int, style: T.() -> Unit = {}, block: T.() -> Unit = {},
  ) = child<T, CoordLayoutParams>(IntSize(width), IntSize(height), style, block)
  
  inline fun <reified T : View> ConstraintLayout.child(
    width: Size, height: Size, style: T.() -> Unit = {}, block: T.() -> Unit = {},
  ) = child<T, ConstraintLayoutParams>(width, height, style, block)
  
  inline fun <reified T : View> ConstraintLayout.child(
    width: Int, height: Int, style: T.() -> Unit = {}, block: T.() -> Unit = {},
  ) = child<T, ConstraintLayoutParams>(IntSize(width), IntSize(height), style, block)
  
  fun ViewGroup.View(
    width: Size,
    height: Size,
    style: Style<View> = {},
    block: Style<View>,
  ) = when (this) {
    is FrameLayout -> child<View, FrameLayoutParams>(width, height, style, block)
    is LinearLayout -> child<View, LinearLayoutParams>(width, height, style, block)
    is CoordinatorLayout -> child<View, CoordLayoutParams>(width, height, style, block)
    else -> child<View, ViewGroupLayoutParams>(width, height, style, block)
  }
  
  fun ViewGroup.TextView(
    width: Size,
    height: Size,
    style: Style<TextView> = {},
    block: Style<TextView>,
  ) = when (this) {
    is FrameLayout -> child<TextView, FrameLayoutParams>(width, height, style, block)
    is LinearLayout -> child<TextView, LinearLayoutParams>(width, height, style, block)
    is CoordinatorLayout -> child<TextView, CoordLayoutParams>(width, height, style, block)
    else -> child<TextView, ViewGroupLayoutParams>(width, height, style, block)
  }
  
  fun ViewGroup.EditText(
    width: Size,
    height: Size,
    style: EditText.() -> Unit = {},
    block: EditText.() -> Unit,
  ) = when (this) {
    is FrameLayout -> child<EditText, FrameLayoutParams>(width, height, style, block)
    is LinearLayout -> child<EditText, LinearLayoutParams>(width, height, style, block)
    is CoordinatorLayout -> child<EditText, CoordLayoutParams>(width, height, style, block)
    else -> child<EditText, ViewGroupLayoutParams>(width, height, style, block)
  }
  
  fun ViewGroup.RecyclerView(
    width: Size,
    height: Size,
    style: Style<RecyclerView> = {},
    block: Style<RecyclerView>,
  ) = when (this) {
    is FrameLayout -> child<RecyclerView, FrameLayoutParams>(width, height, style, block)
    is LinearLayout -> child<RecyclerView, LinearLayoutParams>(width, height, style, block)
    is CoordinatorLayout -> child<RecyclerView, CoordLayoutParams>(width, height, style, block)
    else -> child<RecyclerView, ViewGroupLayoutParams>(width, height, style, block)
  }
  
  fun ViewGroup.ImageView(
    width: Size,
    height: Size,
    style: Style<ImageView> = {},
    block: Style<ImageView>,
  ) = when (this) {
    is FrameLayout -> child<ImageView, FrameLayoutParams>(width, height, style, block)
    is LinearLayout -> child<ImageView, LinearLayoutParams>(width, height, style, block)
    is CoordinatorLayout -> child<ImageView, CoordLayoutParams>(width, height, style, block)
    else -> child<ImageView, ViewGroupLayoutParams>(width, height, style, block)
  }
  
  fun ViewGroup.ImageView(
    width: Int,
    height: Int,
    style: Style<ImageView> = {},
    block: Style<ImageView>,
  ): ImageView {
    val w = IntSize(width)
    val h = IntSize(height)
    return when (this) {
      is FrameLayout -> child<ImageView, FrameLayoutParams>(w, h, style, block)
      is LinearLayout -> child<ImageView, LinearLayoutParams>(w, h, style, block)
      is CoordinatorLayout -> child<ImageView, CoordLayoutParams>(w, h, style, block)
      else -> child<ImageView, ViewGroupLayoutParams>(w, h, style, block)
    }
  }
  
  fun ViewGroup.FrameLayout(
    width: Size,
    height: Size,
    style: Style<FrameLayout> = {},
    block: Style<FrameLayout> = {},
  ) = when (this) {
    is FrameLayout -> child<FrameLayout, FrameLayoutParams>(width, height, style, block)
    is LinearLayout -> child<FrameLayout, LinearLayoutParams>(width, height, style, block)
    is CoordinatorLayout -> child<FrameLayout, CoordLayoutParams>(width, height, style, block)
    else -> child<FrameLayout, ViewGroupLayoutParams>(width, height, style, block)
  }
  
  fun ViewGroup.VerticalLayout(
    width: Size,
    height: Size,
    style: Style<LinearLayout> = {},
    block: Style<LinearLayout>,
  ): LinearLayout {
    val layout = when (this) {
      is FrameLayout -> child<LinearLayout, FrameLayoutParams>(width, height, style, block)
      is LinearLayout -> child<LinearLayout, LinearLayoutParams>(width, height, style, block)
      is CoordinatorLayout -> child<LinearLayout, CoordLayoutParams>(width, height, style, block)
      else -> child<LinearLayout, ViewGroupLayoutParams>(width, height, style, block)
    }
    return layout.apply { orientation(LinearLayout.VERTICAL) }
  }
  
  fun ViewGroup.ScrollableVerticalLayout(
    width: Size = MatchParent,
    height: Size = MatchParent,
    style: Style<LinearLayout> = {},
    block: Style<LinearLayout>,
  ): ScrollView {
    val layout = when (this) {
      is FrameLayout -> child<ScrollView, FrameLayoutParams>(width, height, {}, {})
      is LinearLayout -> child<ScrollView, LinearLayoutParams>(width, height, {}, {})
      is CoordinatorLayout -> child<ScrollView, CoordLayoutParams>(width, height, {}, {})
      else -> child<ScrollView, ViewGroupLayoutParams>(width, height, {}, {})
    }
    return layout.apply {
      child(MatchParent, WrapContent, style, block).apply {
        orientation(LinearLayout.VERTICAL)
      }
    }
  }
  
  fun ViewGroup.ScrollableConstraintLayout(
    width: Size = MatchParent,
    height: Size = MatchParent,
    style: Style<ConstraintLayout> = {},
    block: Style<ConstraintLayout>,
  ): ScrollView {
    val layout = when (this) {
      is FrameLayout -> child<ScrollView, FrameLayoutParams>(width, height, {}, {})
      is LinearLayout -> child<ScrollView, LinearLayoutParams>(width, height, {}, {})
      is CoordinatorLayout -> child<ScrollView, CoordLayoutParams>(width, height, {}, {})
      else -> child<ScrollView, ViewGroupLayoutParams>(width, height, {}, {})
    }
    return layout.apply { child(MatchParent, MatchParent, style, block) }
  }
  
  fun ViewGroup.HorizontalLayout(
    width: Size,
    height: Size,
    style: Style<LinearLayout> = {},
    block: Style<LinearLayout>,
  ): LinearLayout {
    val layout = when (this) {
      is FrameLayout -> child<LinearLayout, FrameLayoutParams>(width, height, style, block)
      is LinearLayout -> child<LinearLayout, LinearLayoutParams>(width, height, style, block)
      is CoordinatorLayout -> child<LinearLayout, CoordLayoutParams>(width, height, style, block)
      else -> child<LinearLayout, ViewGroupLayoutParams>(width, height, style, block)
    }
    return layout.apply { orientation(LinearLayout.HORIZONTAL) }
  }
  
  inline fun <reified T : View, reified P : ViewGroupLayoutParams> ViewGroup.child(
    width: Size,
    height: Size,
    style: T.() -> Unit = {},
    block: T.() -> Unit
  ): T {
    val viewGroupParams = ViewGroupLayoutParams(
      determineSize(context, width),
      determineSize(context, height)
    )
    val viewConstrictor = T::class.java.getDeclaredConstructor(Context::class.java)
    val paramsConstructor = P::class.java.getDeclaredConstructor(ViewGroupLayoutParams::class.java)
    val child = viewConstrictor.newInstance(context)
    val params = paramsConstructor.newInstance(viewGroupParams)
    addView(child, params)
    return child.apply(style).apply(block)
  }
}
