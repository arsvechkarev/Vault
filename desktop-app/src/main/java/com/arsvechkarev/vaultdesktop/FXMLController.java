package com.arsvechkarev.vaultdesktop;

import com.arsvechkarev.commoncrypto.AesSivTinkCipher;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class FXMLController implements Initializable {

  @FXML
  private Label label;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    String javaVersion = System.getProperty("java.version");
    String javafxVersion = System.getProperty("javafx.version");

    var password = "password123";

    var plaintext = "This is my texxxxt!!!";
    byte[] encrypt = AesSivTinkCipher.INSTANCE.encrypt(password, plaintext);
    label.setText(
        "Hello, JavaFX " + password + "\nRunning on Java " + Arrays.toString(encrypt) + ".");

  }
}