package com.arsvechkarev.vault.core.viewdsl

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.arsvechkarev.vault.core.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.core.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.core.viewdsl.Size.IntSize
import android.view.ViewGroup.LayoutParams as ViewGroupLayoutParams
import android.widget.FrameLayout.LayoutParams as FrameLayoutParams
import android.widget.LinearLayout.LayoutParams as LinearLayoutParams
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
  
  fun Any.RootVerticalLayout(
    width: Size = MatchParent,
    height: Size = MatchParent,
    style: LinearLayout.() -> Unit = {},
    block: LinearLayout.() -> Unit = {}
  ) = LinearLayout(context).size(width, height).apply(style).apply(block).apply {
    orientation(LinearLayout.VERTICAL)
  }
  
  fun Any.RootHorizontalLayout(
    width: Size = MatchParent,
    height: Size = MatchParent,
    style: LinearLayout.() -> Unit = {},
    block: LinearLayout.() -> Unit = {}
  ) = LinearLayout(context).size(width, height).apply(style).apply(block).apply {
    orientation(LinearLayout.HORIZONTAL)
  }
  
  fun Any.RootScrollableVerticalLayout(
    width: Size = MatchParent,
    height: Size = MatchParent,
    style: LinearLayout.() -> Unit = {},
    block: LinearLayout.() -> Unit = {}
  ) = ScrollView(context).size(width, height).apply {
    VerticalLayout(MatchParent, WrapContent, style, block)
  }
  
  fun Any.RootFrameLayout(
    width: Size = MatchParent,
    height: Size = MatchParent,
    style: FrameLayout.() -> Unit = {},
    block: FrameLayout.() -> Unit = {}
  ) = FrameLayout(context).size(width, height).apply(style).apply(block)
  
  fun Any.RootCoordinatorLayout(
    width: Size = MatchParent,
    height: Size = MatchParent,
    style: CoordinatorLayout.() -> Unit = {},
    block: CoordinatorLayout.() -> Unit = {}
  ) = CoordinatorLayout(context).size(width, height).apply(style).apply(block)
  
  inline fun <reified T : View> FrameLayout.child(
    width: Size, height: Size, style: T.() -> Unit = {}, block: T.() -> Unit,
  ) = child<T, FrameLayoutParams>(width, height, style, block)
  
  inline fun <reified T : View> FrameLayout.child(
    width: Int, height: Int, style: T.() -> Unit = {}, block: T.() -> Unit
  ) = child<T, FrameLayoutParams>(IntSize(width), IntSize(height), style, block)
  
  inline fun <reified T : View> LinearLayout.child(
    width: Size, height: Size, style: T.() -> Unit = {}, block: T.() -> Unit
  ) = child<T, LinearLayoutParams>(width, height, style, block)
  
  inline fun <reified T : View> LinearLayout.child(
    width: Int, height: Int, style: T.() -> Unit = {}, block: T.() -> Unit
  ) = child<T, LinearLayoutParams>(IntSize(width), IntSize(height), style, block)
  
  inline fun <reified T : View> CoordinatorLayout.child(
    width: Size, height: Size, style: T.() -> Unit = {}, block: T.() -> Unit,
  ) = child<T, CoordLayoutParams>(width, height, style, block)
  
  inline fun <reified T : View> CoordinatorLayout.child(
    width: Int, height: Int, style: T.() -> Unit = {}, block: T.() -> Unit,
  ) = child<T, CoordLayoutParams>(IntSize(width), IntSize(height), style, block)
  
  fun ViewGroup.TextView(
    width: Size,
    height: Size,
    style: TextView.() -> Unit = {},
    block: TextView.() -> Unit,
  ) = when (this) {
    is FrameLayout -> child<TextView, FrameLayoutParams>(width, height, style, block)
    is LinearLayout -> child<TextView, LinearLayoutParams>(width, height, style, block)
    is CoordinatorLayout -> child<TextView, CoordLayoutParams>(width, height, style, block)
    else -> child<TextView, ViewGroupLayoutParams>(width, height, style, block)
  }
  
  fun ViewGroup.View(
    width: Size,
    height: Size,
    style: View.() -> Unit = {},
    block: View.() -> Unit,
  ) = when (this) {
    is FrameLayout -> child<View, FrameLayoutParams>(width, height, style, block)
    is LinearLayout -> child<View, LinearLayoutParams>(width, height, style, block)
    is CoordinatorLayout -> child<View, CoordLayoutParams>(width, height, style, block)
    else -> child<View, ViewGroupLayoutParams>(width, height, style, block)
  }
  
  fun ViewGroup.ImageView(
    width: Size,
    height: Size,
    style: ImageView.() -> Unit = {},
    block: ImageView.() -> Unit,
  ) = when (this) {
    is FrameLayout -> child<ImageView, FrameLayoutParams>(width, height, style, block)
    is LinearLayout -> child<ImageView, LinearLayoutParams>(width, height, style, block)
    is CoordinatorLayout -> child<ImageView, CoordLayoutParams>(width, height, style, block)
    else -> child<ImageView, ViewGroupLayoutParams>(width, height, style, block)
  }
  
  fun ViewGroup.ImageView(
    width: Int,
    height: Int,
    style: ImageView.() -> Unit = {},
    block: ImageView.() -> Unit,
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
    style: FrameLayout.() -> Unit = {},
    block: FrameLayout.() -> Unit,
  ) = when (this) {
    is FrameLayout -> child<FrameLayout, FrameLayoutParams>(width, height, style, block)
    is LinearLayout -> child<FrameLayout, LinearLayoutParams>(width, height, style, block)
    is CoordinatorLayout -> child<FrameLayout, CoordLayoutParams>(width, height, style, block)
    else -> child<FrameLayout, ViewGroupLayoutParams>(width, height, style, block)
  }
  
  fun ViewGroup.VerticalLayout(
    width: Size,
    height: Size,
    style: LinearLayout.() -> Unit = {},
    block: LinearLayout.() -> Unit,
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
    style: LinearLayout.() -> Unit = {},
    block: LinearLayout.() -> Unit,
  ): ScrollView {
    val layout = when (this) {
      is FrameLayout -> child<ScrollView, FrameLayoutParams>(width, height, {}, {})
      is LinearLayout -> child<ScrollView, LinearLayoutParams>(width, height, {}, {})
      is CoordinatorLayout -> child<ScrollView, CoordLayoutParams>(width, height, {}, {})
      else -> child<ScrollView, ViewGroupLayoutParams>(width, height, {}, {})
    }
    return layout.apply {
      child<LinearLayout>(MatchParent, WrapContent, style, block).apply {
        orientation(LinearLayout.VERTICAL)
      }
    }
  }
  
  fun ViewGroup.HorizontalLayout(
    width: Size,
    height: Size,
    style: LinearLayout.() -> Unit = {},
    block: LinearLayout.() -> Unit,
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
      context.determineSize(width),
      context.determineSize(height)
    )
    val viewConstrictor = T::class.java.getDeclaredConstructor(Context::class.java)
    val paramsConstructor = P::class.java.getDeclaredConstructor(ViewGroupLayoutParams::class.java)
    val child = viewConstrictor.newInstance(context)
    val params = paramsConstructor.newInstance(viewGroupParams)
    addView(child, params)
    return child.apply(style).apply(block)
  }
}