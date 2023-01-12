package com.arsvechkarev.vault.core.extensions

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

fun bundleOf(vararg pairs: Pair<String, Any?>): Bundle {
  return Bundle().apply {
    pairs.forEach { pair ->
      when (val value = pair.second) {
        is String -> putString(pair.first, value)
        is Int -> putInt(pair.first, value)
        is Parcelable -> putParcelable(pair.first, value)
        is Serializable -> putSerializable(pair.first, value)
        null -> putParcelable(pair.first, null)
        else -> throw IllegalStateException("Unknown type: $value")
      }
    }
  }
}
