plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.kotlin.android) apply false
}

ext {
  set("minSdk", 26)
  set("compileSdk", 34)
  set("targetSdk", 34)
  set("versionCode", 1)
  set("versionName", "1.0.0")
}

object E {
  val x = 2
}