import java.io.FileInputStream
import java.util.Properties

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  id("kotlin-parcelize")
}

kotlin {
  jvmToolchain(17)
}

android {
  namespace = "com.arsvechkarev.vault"
  buildFeatures.buildConfig = true
  packaging {
    resources {
      excludes += listOf("META-INF/LICENSE", "META-INF/INDEX.LIST")
    }
  }
  val keystoreProperties = file("../../keystore.properties")
  if (keystoreProperties.exists()) {
    signingConfigs {
      val properties = Properties()
      properties.load(FileInputStream(keystoreProperties))
      create("release") {
        storeFile = file(properties.getProperty("keystoreFile"))
        storePassword = properties.getProperty("keystorePassword")
        keyAlias = properties.getProperty("keyAlias")
        keyPassword = properties.getProperty("keyPassword")
      }
    }
  }
  compileSdk = rootProject.extra["compileSdk"].toString().toInt()
  defaultConfig {
    applicationId = "com.arsvechkarev.vault"
    minSdk = rootProject.extra["minSdk"].toString().toInt()
    targetSdk = rootProject.extra["targetSdk"].toString().toInt()
    versionCode = rootProject.extra["versionCode"].toString().toInt()
    versionName = rootProject.extra["versionName"].toString()
    signingConfig = if (keystoreProperties.exists()) {
      signingConfigs.findByName("release")
    } else {
      signingConfigs.findByName("debug")
    }
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
  buildTypes {
    debug {
      buildConfigField("String", "STUB_PASSWORD", "\"qwetu1233\"")
      applicationIdSuffix = ".debug"
    }
    release {
      buildConfigField("String", "STUB_PASSWORD", "\"\"")
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
}

dependencies {
  
  implementation(project(":android-app:domain"))
  implementation(project(":android-app:navigation"))
  implementation(project(":android-app:viewdsl"))
  
  implementation(libs.kotlinStdlib)
  
  implementation(libs.kotlinStdlib)
  implementation(libs.appCompat)
  implementation(libs.coroutinesCore)
  implementation(libs.coroutinesAndroid)
  implementation(libs.coordinatorLayout)
  implementation(libs.constraintLayout)
  implementation(libs.recyclerView)
  implementation(libs.kotpass)
  implementation(libs.okio)
  implementation(libs.zxcvbn)
  implementation(libs.timber)
  implementation(libs.lifecycleKtx)
  implementation(libs.coil)
  implementation(libs.biometrics)
  implementation(libs.documentfile)
  
  testImplementation(libs.test.junit)
  testImplementation(libs.test.coroutinesTest)
  
  androidTestImplementation(libs.test.kaspresso)
  androidTestImplementation(libs.test.espressoIntents)
  androidTestImplementation(libs.test.androidTestRunner)
  androidTestImplementation(libs.test.androidTestRules)
  androidTestImplementation(libs.test.androidTestExt)
  androidTestImplementation(libs.test.androidTestCoreKtx)
}
