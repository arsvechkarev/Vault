package com.arsvechkarev.vault.tests

import com.arsvechkarev.vault.features.common.domain.images_names.ImageNameData
import com.arsvechkarev.vault.features.common.domain.images_names.ImageNameData.Basic
import com.arsvechkarev.vault.features.common.domain.images_names.ImageNameData.Compound
import org.junit.Assert.assertEquals
import org.junit.Test

class ImageNameDataTest {
  
  private val stringData = """
    amazon
    google
    proton mail,protonmail|protonmail
    one,two|three
  """.trimIndent()
  
  private val imagesNames = listOf(
    Basic("amazon"),
    Basic("google"),
    Compound("protonmail", listOf("proton mail", "protonmail")),
    Compound("three", listOf("one", "two")),
  )
  
  @Test
  fun testParsingString() {
    assertEquals(imagesNames, ImageNameData.parse(stringData))
  }
  
  @Test
  fun testToString() {
    assertEquals(stringData, ImageNameData.toString(imagesNames))
  }
}
