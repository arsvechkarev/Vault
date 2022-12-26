package com.arsvechkarev.vault.test.core.ext

import android.widget.TextView
import androidx.test.espresso.assertion.ViewAssertions
import buisnesslogic.generator.PasswordGeneratorImpl.Companion.createSymbolsGeneratorsList
import buisnesslogic.generator.SecureRandomGenerator
import buisnesslogic.model.PasswordCharacteristic
import buisnesslogic.model.PasswordCharacteristic.NUMBERS
import buisnesslogic.model.PasswordCharacteristic.SPECIAL_SYMBOLS
import buisnesslogic.model.PasswordCharacteristic.UPPERCASE_SYMBOLS
import com.arsvechkarev.vault.test.core.base.baseMatcher
import io.github.kakaocup.kakao.text.TextViewAssertions
import java.util.EnumSet

fun TextViewAssertions.hasTextLength(length: Int) {
  view.check(
    ViewAssertions.matches(
      baseMatcher<TextView>(
        descriptionText = "has text length $length",
        matcher = { textView -> textView.text.length == length }
      )
    )
  )
}

fun TextViewAssertions.hasTextColorInt(color: Int) {
  view.check(
    ViewAssertions.matches(
      baseMatcher<TextView>(
        descriptionText = "has text colorInt $color",
        matcher = { textView -> textView.currentTextColor == color }
      )
    )
  )
}

fun TextViewAssertions.hasPasswordWithAllCharacteristics() {
  hasPasswordWithCharacteristics(UPPERCASE_SYMBOLS, NUMBERS, SPECIAL_SYMBOLS)
}

fun TextViewAssertions.hasPasswordWithNoCharacteristics() {
  hasPasswordWithCharacteristics()
}

fun TextViewAssertions.hasPasswordWithCharacteristics(
  vararg characteristic: PasswordCharacteristic
) {
  val characteristics = if (characteristic.isNotEmpty()) {
    EnumSet.copyOf(characteristic.toList())
  } else {
    EnumSet.noneOf(PasswordCharacteristic::class.java)
  }
  val generators = createSymbolsGeneratorsList(SecureRandomGenerator, characteristics)
  view.check(
    ViewAssertions.matches(
      baseMatcher<TextView>(
        descriptionText = "matches password characteristics: $characteristics",
        matcher = { textView ->
          generators.all { generator ->
            generator.isPresentInPassword(textView.text.toString())
          }
        }
      )
    )
  )
}
