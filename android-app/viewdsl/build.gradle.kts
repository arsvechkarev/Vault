plugins {
  id("com.android.library")
  id("kotlin-android")
}

kotlin {
  jvmToolchain(17)
}

android {
  namespace = "com.arsvechkarev.viewdsl"
  compileSdk = rootProject.extra["compileSdk"].toString().toInt()
}

dependencies {
  implementation(libs.kotlinStdlib)
  implementation(libs.appCompat)
  implementation(libs.androidXCore)
  implementation(libs.coordinatorLayout)
  implementation(libs.constraintLayout)
  implementation(libs.recyclerView)
}
