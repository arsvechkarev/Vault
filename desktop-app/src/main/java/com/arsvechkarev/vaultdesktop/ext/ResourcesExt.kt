package com.arsvechkarev.vaultdesktop.ext

import com.arsvechkarev.vaultdesktop.VaultLauncher
import java.io.InputStream
import java.net.URL

private const val BASE_PATH = "/com/arsvechkarev/vaultdesktop"

fun loadResource(relativePath: String): URL {
  return requireNotNull(VaultLauncher::class.java
      .getResource("$BASE_PATH/$relativePath"))
}

fun loadResourceAsStream(relativePath: String): InputStream {
  return requireNotNull(VaultLauncher::class.java
      .getResourceAsStream("$BASE_PATH/$relativePath"))
}
