package com.arsvechkarev.vaultdesktop.controllers

import com.arsvechkarev.commoncrypto.AesSivTinkCipher
import com.arsvechkarev.vaultdesktop.style.Colors
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.paint.Paint
import javafx.stage.FileChooser
import java.io.File

class LoginController {
  
  @FXML
  private lateinit var filenameHeader: Label
  
  @FXML
  private lateinit var filenameLabel: Label
  
  @FXML
  private lateinit var masterPasswordLabel: Label
  
  @FXML
  private lateinit var masterPasswordField: PasswordField
  
  private var file: File? = null
  
  @FXML
  fun onOpenFileClick() {
    filenameHeader.text = "File"
    filenameHeader.textFill = Paint.valueOf(Colors.ACCENT)
    file = FileChooser().apply { title = "Select passwords vault" }
        .showOpenDialog(filenameLabel.scene.window) ?: return
    filenameLabel.text = file!!.absolutePath
  }
  
  @FXML
  fun onKeyPressed(event: KeyEvent) {
    if (event.code == KeyCode.ENTER) {
      onContinueClick()
    } else {
      masterPasswordLabel.text = "Master password"
      masterPasswordLabel.textFill = Paint.valueOf(Colors.ACCENT)
    }
  }
  
  @FXML
  fun onContinueClick() {
    val file = file
    if (file == null) {
      filenameHeader.text = "File is not selected"
      filenameHeader.textFill = Paint.valueOf(Colors.ERROR)
      return
    }
    val password = masterPasswordField.text
    if (password.isNullOrBlank()) {
      masterPasswordLabel.text = "Password is empty"
      masterPasswordLabel.textFill = Paint.valueOf(Colors.ERROR)
      return
    }
    
    val data = file.readBytes()
    runCatching { AesSivTinkCipher.decrypt(password, data) }
        .onFailure {
          masterPasswordLabel.text = "Password is incorrect"
          masterPasswordLabel.textFill = Paint.valueOf(Colors.ERROR)
        }
        .onSuccess {
          println("================================")
          println(it)
          println("================================")
        }
  }
}
