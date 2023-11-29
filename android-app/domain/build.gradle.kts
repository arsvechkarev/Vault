plugins {
  id("kotlin")
}

dependencies {
  implementation(libs.kotlinStdlib)
  implementation(libs.coroutinesCore)
  implementation(libs.zxcvbn)
  implementation(libs.kotpass)
  implementation(libs.okio)
}
