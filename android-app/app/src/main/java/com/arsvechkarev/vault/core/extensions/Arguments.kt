package com.arsvechkarev.vault.core.extensions

import android.os.Parcelable
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

fun <T : Parcelable> Fragment.arg(klass: KClass<out T>): T {
  return checkNotNull(arguments?.getParcelable(klass.qualifiedName))
}
