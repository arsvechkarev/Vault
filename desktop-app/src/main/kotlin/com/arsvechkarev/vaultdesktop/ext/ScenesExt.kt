package com.arsvechkarev.vaultdesktop.ext

import com.arsvechkarev.vaultdesktop.VaultDesktopApplication
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage

fun openNewScene(sceneFxml: String, stage: Stage? = null, closeCurrentStage: Boolean = false) {
  val fxmlLoader = FXMLLoader(VaultDesktopApplication::class.java.getResource(sceneFxml))
  val scene = Scene(fxmlLoader.load()).apply {
    stylesheets.add(VaultDesktopApplication::class.java.getResource("style.css").toExternalForm())
  }
  if (closeCurrentStage && stage != null) {
    stage.close()
  }
  (stage ?: Stage()).apply {
    centerOnScreen()
    isResizable = false
    icons.add(Image(VaultDesktopApplication::class.java.getResourceAsStream("icons/icon.png")))
    title = "Vault"
    this.scene = scene
    show()
  }
}
