package com.arsvechkarev.vault.core

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

/*
 * ========================================================
 * =============== COPIED FROM ANDROID SDK  ===============
 * ========================================================
 */
/**
 * Span that updates the typeface of the text it's attached to. The
 * `TypefaceSpan` can be constructed either based on a font family or based on
 * a `Typeface`. When [TypefaceSpan] is used, the previous style
 * of the `TextView` is kept. When [TypefaceSpan] is used,
 * the
 * `Typeface` style replaces the
 * `TextView`'s style.
 *
 *
 * For example, let's consider a `TextView` with
 * `android:textStyle="italic"` and a typeface created based on a font from
 * resources, with a bold style. When applying a `TypefaceSpan` based the
 * typeface, the text will only keep the bold style, overriding the
 * `TextView`'s textStyle. When applying a
 * `TypefaceSpan` based on a font family: "monospace", the resulted text will
 * keep the italic style.
 * <pre>
 * Typeface myTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.acme),
 * Typeface.BOLD);
 * SpannableString string = new SpannableString("Text with typeface span.");
 * string.setSpan(new TypefaceSpan(myTypeface), 10, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
 * string.setSpan(new TypefaceSpan("monospace"), 19, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
</pre> *
 * <img src="{@docRoot}reference/android/images/text/style/typefacespan.png"></img>
 * <figcaption>Text with `TypefaceSpan`s constructed based on a font from
 * resource and from a font family.</figcaption>
 */
/*
 * ========================================================
 * =============== COPIED FROM ANDROID SDK  ===============
 * ========================================================
 */
class TypefaceSpan(
  private val mTypeface: Typeface?,
  private val mFamily: String? = null,
) : MetricAffectingSpan() {
  
  override fun updateDrawState(ds: TextPaint) {
    updateTypeface(ds)
  }
  
  override fun updateMeasureState(paint: TextPaint) {
    updateTypeface(paint)
  }
  
  private fun updateTypeface(paint: Paint) {
    if (mTypeface != null) {
      paint.setTypeface(mTypeface)
    } else {
      mFamily?.let { family -> applyFontFamily(paint, family) }
    }
  }
  
  private fun applyFontFamily(paint: Paint, family: String) {
    val old = paint.typeface
    val style = old?.style ?: Typeface.NORMAL
    val styledTypeface = Typeface.create(family, style)
    val fake = style and styledTypeface.style.inv()
    if (fake and Typeface.BOLD != 0) {
      paint.isFakeBoldText = true
    }
    if (fake and Typeface.ITALIC != 0) {
      paint.textSkewX = -0.25f
    }
    paint.setTypeface(styledTypeface)
  }
}
