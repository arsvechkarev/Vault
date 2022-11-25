package viewdsl

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.*
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.coordinatorlayout.widget.CoordinatorLayout

operator fun View.contains(event: MotionEvent): Boolean {
    val x = event.x
    val y = event.y
    return x >= left + translationX
            && y >= top + translationY
            && x <= right + translationX
            && y <= bottom + translationY
}

fun View.layoutAroundPoint(x: Int, y: Int) {
    layout(
        x - measuredWidth / 2,
        y - measuredHeight / 2,
        x + measuredWidth / 2,
        y + measuredHeight / 2
    )
}

fun View.layoutLeftTop(left: Int, top: Int) {
    layout(left, top, left + measuredWidth, top + measuredHeight)
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

var View.isVisible
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.INVISIBLE
    }

fun <T : View> T.size(
    width: Int,
    height: Int,
    margins: Margins = Margins()
): T {
    size(Size.IntSize(width), Size.IntSize(height), margins)
    return this
}

fun <T : View> T.size(
    width: Size,
    height: Size,
    margins: Margins = Margins()
): T {
    if (layoutParams == null) {
        layoutParams = context.createLayoutParams(width, height, margins)
    } else {
        layoutParams.width = determineSize(context, width)
        layoutParams.height = determineSize(context, height)
    }
    return this
}

fun View.layoutGravity(gravity: Int) {
    when (val params = layoutParams) {
        is FrameLayout.LayoutParams -> params.gravity = gravity
        is LinearLayout.LayoutParams -> params.gravity = gravity
        is CoordinatorLayout.LayoutParams -> params.gravity = gravity
        else -> throw IllegalStateException(
            "Unable to set gravity to " +
                    "layout params ${params.javaClass.name}"
        )
    }
}

val View.marginStart get() = (layoutParams as? MarginLayoutParams)?.marginStart ?: 0

val View.marginTop get() = (layoutParams as? MarginLayoutParams)?.topMargin ?: 0

val View.marginEnd get() = (layoutParams as? MarginLayoutParams)?.marginEnd ?: 0

val View.marginBottom get() = (layoutParams as? MarginLayoutParams)?.bottomMargin ?: 0

fun View.margin(value: Int) {
    margins(value, value, value, value)
}

fun View.marginVertical(value: Int) {
    margins(marginStart, value, marginEnd, value)
}

fun View.marginHorizontal(value: Int) {
    margins(value, marginTop, value, marginBottom)
}

fun View.margins(
    start: Int = marginStart,
    top: Int = marginTop,
    end: Int = marginEnd,
    bottom: Int = marginBottom
) {
    if (layoutParams is MarginLayoutParams) {
        val params = layoutParams as MarginLayoutParams
        if (isLayoutLeftToRight) {
            params.setMargins(start, top, end, bottom)
        } else {
            params.setMargins(end, top, start, bottom)
        }
    } else {
        val params = MarginLayoutParams(layoutParams)
        if (isLayoutLeftToRight) {
            params.setMargins(start, top, end, bottom)
        } else {
            params.setMargins(end, top, start, bottom)
        }
        layoutParams = params
    }
}

fun View.padding(value: Int) {
    paddings(value, value, value, value)
}

fun View.paddingVertical(value: Int) {
    paddings(paddingStart, value, paddingEnd, value)
}

fun View.paddingHorizontal(value: Int) {
    paddings(value, paddingTop, value, paddingBottom)
}

fun View.paddings(
    start: Int = paddingStart,
    top: Int = paddingTop,
    end: Int = paddingEnd,
    bottom: Int = paddingBottom
) {
    if (isLayoutLeftToRight) {
        setPadding(start, top, end, bottom)
    } else {
        setPadding(end, top, start, bottom)
    }
}

fun View.string(stringRes: Int): String {
    return resources.getString(stringRes)
}

fun onClick(vararg views: View, action: (View) -> Unit) {
    views.forEach { it.setOnClickListener(action) }
}

fun setClickable(isClickable: Boolean, vararg views: View) {
    views.forEach { it.isClickable = isClickable }
}

fun View.clearOnClick() {
    setOnClickListener(null)
}

fun View.onClick(block: () -> Unit) {
    setOnClickListener { block() }
}

fun View.onLongClick(block: () -> Unit) {
    setOnLongClickListener { block(); true }
}

fun View.tag(tag: Any) {
    this.tag = tag
}

fun <T : View> T.classNameTag() {
    this.tag = javaClass.name
}

fun View.id(id: Int) {
    this.id = id
}

fun View.background(drawable: Drawable?) {
    background = drawable
}

fun View.backgroundGradient(orientation: GradientDrawable.Orientation, vararg colors: Int) {
    background = GradientDrawable(orientation, colors)
}

fun View.backgroundColor(@ColorInt color: Int) {
    setBackgroundColor(color)
}

fun View.background(@DrawableRes drawableRes: Int) {
    background = context.retrieveDrawable(drawableRes)
}

fun View.behavior(behavior: CoordinatorLayout.Behavior<*>) {
    (layoutParams as CoordinatorLayout.LayoutParams).behavior = behavior
}

inline fun <reified T : CoordinatorLayout.Behavior<*>> View.getBehavior(): T {
    return (layoutParams as CoordinatorLayout.LayoutParams).behavior as T
}

inline fun <reified T : CoordinatorLayout.Behavior<*>> View.hasBehavior(): Boolean {
    return (layoutParams as? CoordinatorLayout.LayoutParams)?.behavior as? T != null
}

inline fun View.onLayoutChanged(crossinline block: () -> Unit) {
  addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> block() }
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : View> View.viewAs(tag: Any = T::class.java.name): T {
  return findViewWithTag(tag)
}

fun View.textView(tag: Any): TextView = findViewWithTag(tag)

fun View.editText(tag: Any): EditText = findViewWithTag(tag)

fun View.imageView(tag: Any): ImageView = findViewWithTag(tag)