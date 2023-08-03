package com.arsvechkarev.vaultdesktop.controllers

import com.arsvechkarev.commoncrypto.AesSivTinkCipher
import com.arsvechkarev.vaultdesktop.EntriesHolder
import com.arsvechkarev.vaultdesktop.ext.openNewScene
import com.arsvechkarev.vaultdesktop.model.Entries
import com.arsvechkarev.vaultdesktop.style.Colors
import com.google.gson.Gson
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.input.InputEvent
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.paint.Paint
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File

class LoginController {
  
  @FXML
  private lateinit var labelFilenameTitle: Label
  
  @FXML
  private lateinit var labelFilenameValue: Label
  
  @FXML
  private lateinit var labelMasterPassword: Label
  
  @FXML
  private lateinit var masterPasswordField: PasswordField
  
  private var file: File? = null
  private val gson = Gson()
  
  @FXML
  fun onOpenFileClick() {
    labelFilenameTitle.text = "File"
    labelFilenameTitle.textFill = Paint.valueOf(Colors.ACCENT)
    file = FileChooser().apply { title = "Select passwords vault" }
        .showOpenDialog(labelFilenameValue.scene.window) ?: return
    labelFilenameValue.text = file!!.absolutePath
  }
  
  @FXML
  fun onKeyPressed(event: KeyEvent) {
    if (event.code == KeyCode.ENTER) {
      onContinueClick(event)
    } else {
      labelMasterPassword.text = "Master password"
      labelMasterPassword.textFill = Paint.valueOf(Colors.ACCENT)
    }
  }
  
  @FXML
  fun onContinueClick(event: InputEvent) {
    val file = file
    if (file == null) {
      labelFilenameTitle.text = "File is not selected"
      labelFilenameTitle.textFill = Paint.valueOf(Colors.ERROR)
      return
    }
    val password = masterPasswordField.text
    if (password.isNullOrBlank()) {
      labelMasterPassword.text = "Password is empty"
      labelMasterPassword.textFill = Paint.valueOf(Colors.ERROR)
      return
    }
    
    val data = file.readBytes()
    runCatching { AesSivTinkCipher.decrypt(password, data) }
        .onFailure {
          labelMasterPassword.text = "Password is incorrect"
          labelMasterPassword.textFill = Paint.valueOf(Colors.ERROR)
        }
        .onSuccess { json ->
          EntriesHolder.entries = gson.fromJson(json, Entries::class.java)
          openNewScene(
            sceneFxml = "scene_main.fxml",
            stage = (event.source as Node).scene.window as Stage,
            closeCurrentStage = true
          )
        }
  }
}
