pluginManagement {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
  }
}
rootProject.name = "Vault"

include("android-app:app")
include("android-app:navigation")
include("android-app:domain")
include("android-app:viewdsl")
