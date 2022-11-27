package com.arsvechkarev.vault.viewbuilding;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Span that updates the typeface of the text it's attached to. The
 * <code>TypefaceSpan</code> can be constructed either based on a font family or based on
 * a <code>Typeface</code>. When {@link #TypefaceSpan(String)} is used, the previous style
 * of the <code>TextView</code> is kept. When {@link #TypefaceSpan(Typeface)} is used,
 * the
 * <code>Typeface</code> style replaces the
 * <code>TextView</code>'s style.
 * <p>
 * For example, let's consider a <code>TextView</code> with
 * <code>android:textStyle="italic"</code> and a typeface created based on a font from
 * resources, with a bold style. When applying a <code>TypefaceSpan</code> based the
 * typeface, the text will only keep the bold style, overriding the
 * <code>TextView</code>'s textStyle. When applying a
 * <code>TypefaceSpan</code> based on a font family: "monospace", the resulted text will
 * keep the italic style.
 * <pre>
 * Typeface myTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.acme),
 * Typeface.BOLD);
 * SpannableString string = new SpannableString("Text with typeface span.");
 * string.setSpan(new TypefaceSpan(myTypeface), 10, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
 * string.setSpan(new TypefaceSpan("monospace"), 19, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
 * </pre>
 * <img src="{@docRoot}reference/android/images/text/style/typefacespan.png" />
 * <figcaption>Text with <code>TypefaceSpan</code>s constructed based on a font from
 * resource and from a font family.</figcaption>
 */
public class TypefaceSpan extends MetricAffectingSpan {

    private final String mFamily;

    private final Typeface mTypeface;

    /**
     * Constructs a {@link TypefaceSpan} based on the font family. The previous style of the
     * TextPaint is kept. If the font family is null, the text paint is not modified.
     *
     * @param family The font family for this typeface.  Examples include "monospace",
     *               "serif", and "sans-serif"
     */
    public TypefaceSpan(@Nullable String family) {
        this(family, null);
    }

    /**
     * Constructs a {@link TypefaceSpan} from a {@link Typeface}. The previous style of the
     * TextPaint is overridden and the style of the typeface is used.
     *
     * @param typeface the typeface
     */
    public TypefaceSpan(@NonNull Typeface typeface) {
        this(null, typeface);
    }

    private TypefaceSpan(@Nullable String family, @Nullable Typeface typeface) {
        mFamily = family;
        mTypeface = typeface;
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        updateTypeface(ds);
    }

    @Override
    public void updateMeasureState(@NonNull TextPaint paint) {
        updateTypeface(paint);
    }

    private void updateTypeface(@NonNull Paint paint) {
        if (mTypeface != null) {
            paint.setTypeface(mTypeface);
        } else if (mFamily != null) {
            applyFontFamily(paint, mFamily);
        }
    }

    private void applyFontFamily(@NonNull Paint paint, @NonNull String family) {
        int style;
        Typeface old = paint.getTypeface();
        if (old == null) {
            style = Typeface.NORMAL;
        } else {
            style = old.getStyle();
        }
        final Typeface styledTypeface = Typeface.create(family, style);
        int fake = style & ~styledTypeface.getStyle();

        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }
        paint.setTypeface(styledTypeface);
    }
}
