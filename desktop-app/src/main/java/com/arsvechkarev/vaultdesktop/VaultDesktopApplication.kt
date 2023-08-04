package com.arsvechkarev.vaultdesktop

import com.arsvechkarev.vaultdesktop.ext.openNewScene
import javafx.application.Application
import javafx.stage.Stage

class VaultDesktopApplication : Application() {
  
  override fun start(primaryStage: Stage) {
    openNewScene("scene_login.fxml", primaryStage)
  }
}
