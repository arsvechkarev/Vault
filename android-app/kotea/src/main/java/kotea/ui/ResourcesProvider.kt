package kotea.ui

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat

interface ResourcesProvider {

    fun getString(@StringRes resource: Int, vararg args: Any?): String

    fun getQuantityString(@PluralsRes resource: Int, quantity: Int, vararg args: Any?): String

    fun getStringArray(@ArrayRes resource: Int): Array<String>

    @ColorInt
    fun getColor(@ColorRes resource: Int): Int

    fun getDrawable(@DrawableRes drawableId: Int): Drawable

    @Px
    fun getDimensionPixelSize(@DimenRes dimen: Int): Int
}

internal class ContextResourcesProvider(private val context: Context) : ResourcesProvider {

    override fun getString(resource: Int, vararg args: Any?): String {
        return context.resources.getString(resource, *args)
    }

    override fun getQuantityString(resource: Int, quantity: Int, vararg args: Any?): String {
        return context.resources.getQuantityString(resource, quantity, *args)
    }

    override fun getStringArray(resource: Int): Array<String> {
        return context.resources.getStringArray(resource)
    }

    override fun getColor(resource: Int): Int {
        return ContextCompat.getColor(context, resource)
    }

    override fun getDrawable(drawableId: Int): Drawable {
        return checkNotNull(ContextCompat.getDrawable(context, drawableId)) {
            "Unable to get drawable from resources. Check passed drawable ID."
        }
    }

    override fun getDimensionPixelSize(dimen: Int): Int {
        return context.resources.getDimensionPixelSize(dimen)
    }
}
