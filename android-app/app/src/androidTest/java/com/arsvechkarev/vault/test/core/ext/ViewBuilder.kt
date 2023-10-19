package com.arsvechkarev.vault.test.core.ext

import android.view.View
import io.github.kakaocup.kakao.common.builders.ViewBuilder

inline fun <reified V : View> ViewBuilder.withClassNameTag() {
  withTag(V::class.java.name)
}
