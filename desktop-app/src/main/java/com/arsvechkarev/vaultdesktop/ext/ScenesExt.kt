package com.arsvechkarev.vaultdesktop.ext

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage

@JvmOverloads
fun openNewScene(sceneFxml: String, stage: Stage? = null, closeCurrentStage: Boolean = false) {
  val fxmlLoader = FXMLLoader(loadResource(sceneFxml))
  val scene = Scene(fxmlLoader.load()).apply {
    stylesheets.add(loadResource("style.css").toExternalForm())
  }
  if (closeCurrentStage && stage != null) {
    stage.close()
  }
  (stage ?: Stage()).apply {
    centerOnScreen()
    isResizable = false
    icons.add(Image(loadResourceAsStream("icons/icon.png")))
    title = "Vault"
    this.scene = scene
    show()
  }
}
