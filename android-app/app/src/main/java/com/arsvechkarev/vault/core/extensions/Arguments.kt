package com.arsvechkarev.vault.core.extensions

import android.os.Build
import android.os.Parcelable
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

fun <T : Parcelable> Fragment.arg(klass: KClass<out T>): T {
  return checkNotNull(nullableArg(klass))
}

fun <T : Parcelable> Fragment.nullableArg(klass: KClass<out T>): T? {
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    arguments?.getParcelable(klass.qualifiedName, klass.java)
  } else {
    @Suppress("DEPRECATION")
    arguments?.getParcelable(klass.qualifiedName)
  }
}
