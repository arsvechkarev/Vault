package com.arsvechkarev.vaultdesktop

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import java.net.URL

class VaultDesktopApplication : Application() {
  
  override fun start(stage: Stage) {
    val fxmlLoader = FXMLLoader(getResource("screen_login.fxml"))
    val scene = Scene(fxmlLoader.load()).apply {
      stylesheets.add(getResource("style.css").toExternalForm())
    }
    stage.apply {
      isResizable = false
      icons.add(Image(VaultDesktopApplication::class.java.getResourceAsStream("icon.png")))
      title = "Vault"
      this.scene = scene
      show()
    }
  }
  
  private fun getResource(name: String): URL {
    return VaultDesktopApplication::class.java.getResource(name)
  }
}

fun main() {
  Application.launch(VaultDesktopApplication::class.java)
}
