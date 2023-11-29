plugins {
  id("com.android.library")
  id("kotlin-android")
}

kotlin {
  jvmToolchain(17)
}

android {
  namespace = "navigation"
  compileSdk = rootProject.extra["compileSdk"].toString().toInt()
  lint {
    baseline = file("lint-baseline.xml")
  }
}

dependencies {
  implementation(libs.kotlinStdlib)
  implementation(libs.appCompat)
}
