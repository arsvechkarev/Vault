package com.arsvechkarev.vaultdesktop.controllers

import com.arsvechkarev.commoncrypto.AesSivTinkCipher
import javafx.fxml.FXML
import javafx.scene.control.Label

class LoginController {
  
  @FXML
  private lateinit var filenameHeader: Label
  
  @FXML
  private lateinit var filenameLabel: Label
  
  @FXML
  private lateinit var masterPasswordLabel: Label
  
  @FXML
  fun onOpenFileClick() {
    val encrypt = AesSivTinkCipher.encrypt("qwertt", "ddfsdf")
    val decrypt = AesSivTinkCipher.decrypt("qwertt", encrypt)
  }
  
  @FXML
  fun onContinueClick() {
    filenameLabel.text = "test"
    val encrypt = AesSivTinkCipher.encrypt("qwertt", "ddfsdf")
    val decrypt = AesSivTinkCipher.decrypt("qwertt", encrypt)
  }
}