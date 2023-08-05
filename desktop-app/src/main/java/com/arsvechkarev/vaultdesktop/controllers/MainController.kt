package com.arsvechkarev.vaultdesktop.controllers

import com.arsvechkarev.vaultdesktop.EntriesHolder
import com.arsvechkarev.vaultdesktop.ext.firstCapitalizedLetter
import com.arsvechkarev.vaultdesktop.ext.loadResourceAsStream
import com.arsvechkarev.vaultdesktop.model.Password
import com.arsvechkarev.vaultdesktop.model.PlainText
import javafx.animation.Interpolator
import javafx.animation.PauseTransition
import javafx.animation.TranslateTransition
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.geometry.Insets
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Paint
import javafx.scene.shape.Circle
import javafx.util.Duration
import java.util.Locale

// TODO (8/3/2023): Extract separate controllers?
class MainController {
  
  @FXML
  private lateinit var anchorPaneContent: AnchorPane
  
  @FXML
  private lateinit var paneNothingSelected: Pane
  
  @FXML
  private lateinit var panePassword: Pane
  
  @FXML
  private lateinit var panePlainText: Pane
  
  @FXML
  private lateinit var vBoxEntries: VBox
  
  @FXML
  private lateinit var labelWebsiteName: Label
  
  @FXML
  private lateinit var labelWebsiteNameLetter: Label
  
  @FXML
  private lateinit var textFieldWebsiteName: TextField
  
  @FXML
  private lateinit var textFieldLogin: TextField
  
  @FXML
  private lateinit var textFieldPassword: TextField
  
  @FXML
  private lateinit var textAreaNotes: TextArea
  
  @FXML
  private lateinit var imageCopyWebsiteName: ImageView
  
  @FXML
  private lateinit var imageCopyLogin: ImageView
  
  @FXML
  private lateinit var imageCopyPassword: ImageView
  
  @FXML
  private lateinit var imageTogglePassword: ImageView
  
  @FXML
  private lateinit var imageCopyNotes: ImageView
  
  @FXML
  private lateinit var textFieldTitle: TextField
  
  @FXML
  private lateinit var textAreaText: TextArea
  
  @FXML
  private lateinit var imageCopyTitle: ImageView
  
  @FXML
  private lateinit var imageCopyText: ImageView
  
  private lateinit var notification: Pane
  
  private var isPasswordHidden = true
  private var currentPassword: Password? = null
  private var currentPlainText: PlainText? = null
  private var animating: Boolean = false
  
  fun initialize() {
    addNotification()
    val entries = requireNotNull(EntriesHolder.entries)
    if (entries.passwords.isNotEmpty()) {
      vBoxEntries.children.add(createTitleLabel("Passwords"))
      val sortedPasswords = entries.passwords.sortedBy {
        it.websiteName.lowercase(Locale.getDefault())
      }
      for (password in sortedPasswords) {
        vBoxEntries.children.add(
          createItem(id = password.id, text = password.websiteName, onClick = ::openPassword)
        )
      }
    }
    if (entries.plainTexts.isNotEmpty()) {
      vBoxEntries.children.add(createTitleLabel("Plain texts"))
      val sortedPlainTexts = entries.plainTexts.sortedBy { it.title.lowercase(Locale.getDefault()) }
      for (plainText in sortedPlainTexts) {
        vBoxEntries.children.add(
          createItem(id = plainText.id, text = plainText.title, onClick = ::openPlainText)
        )
      }
    }
  }
  
  fun onCopyWebsiteNameClicked() {
    copyText(requireNotNull(currentPassword).websiteName)
  }
  
  fun onCopyLoginClicked() {
    copyText(requireNotNull(currentPassword).login)
  }
  
  fun onCopyPasswordClicked() {
    copyText(requireNotNull(currentPassword).password)
  }
  
  fun onTogglePasswordClicked() {
    isPasswordHidden = !isPasswordHidden
    if (isPasswordHidden) {
      textFieldPassword.text = HIDDEN_PASSWORD_STUB
    } else {
      textFieldPassword.text = requireNotNull(currentPassword).password
    }
  }
  
  fun onCopyNotesClicked() {
    copyText(requireNotNull(currentPassword).notes)
  }
  
  fun onCopyTitleClicked() {
    copyText(requireNotNull(currentPlainText).title)
  }
  
  fun onCopyTextClicked() {
    copyText(requireNotNull(currentPlainText).text)
  }
  
  private fun addNotification() {
    notification = Pane().apply {
      layoutX = 175.0
      layoutY = 630.0
      prefWidth = 200.0
      prefHeight = 40.0
      styleClass.add("notification-copy")
      children.add(ImageView().apply {
        image = Image(loadResourceAsStream("icons/check.png"))
        layoutX = 8.0
        layoutY = 5.0
        fitHeight = 30.0
        fitWidth = 30.0
      })
      children.add(Label().apply {
        text = "Successfully copied!"
        layoutX = 57.0
        layoutY = 11.0
      })
    }
    anchorPaneContent.children.add(notification)
  }
  
  private fun createTitleLabel(text: String): Label {
    return Label(text).apply {
      padding = Insets(15.0, 0.0, 5.0, 10.0)
      styleClass.add("title-content")
    }
  }
  
  private fun createItem(id: String, text: String, onClick: (id: String) -> Unit): Pane {
    return Pane().apply {
      prefWidth = 250.0
      prefHeight = 42.0
      onMouseClicked = EventHandler { _ -> onClick(id) }
      children.add(Circle().apply {
        radius = 14.0
        layoutX = 28.0
        layoutY = 21.0
        fill = Paint.valueOf("#FFFFFF")
      })
      children.add(Label().apply {
        this.text = text.firstCapitalizedLetter()
        layoutX = 14.0
        layoutY = 7.0
        prefWidth = 28.0
        prefHeight = 28.0
        styleClass.add("capitalized-letter")
      })
      children.add(Label().apply {
        this.text = text
        layoutX = 53.0
        layoutY = 11.0
        prefWidth = 183.0
        prefHeight = 20.0
      })
    }
  }
  
  private fun openPassword(id: String) {
    isPasswordHidden = true
    val password = requireNotNull(EntriesHolder.entries).passwords.find { it.id == id } as Password
    currentPassword = password
    selectPane(panePassword)
    labelWebsiteNameLetter.text = password.websiteName.firstCapitalizedLetter()
    labelWebsiteName.text = password.websiteName
    textFieldWebsiteName.text = password.websiteName
    textFieldLogin.text = password.login
    textFieldPassword.text = HIDDEN_PASSWORD_STUB
    textAreaNotes.text = password.notes
  }
  
  private fun openPlainText(id: String) {
    val plainText = requireNotNull(
      EntriesHolder.entries).plainTexts.find { it.id == id } as PlainText
    currentPlainText = plainText
    selectPane(panePlainText)
    textFieldTitle.text = plainText.title
    textAreaText.text = plainText.text
  }
  
  private fun selectPane(pane: Pane) {
    panePassword.isVisible = false
    paneNothingSelected.isVisible = false
    panePlainText.isVisible = false
    pane.isVisible = true
  }
  
  private fun copyText(text: String) {
    Clipboard.getSystemClipboard().setContent(ClipboardContent().apply { putString(text) })
    showCopyNotification()
  }
  
  private fun showCopyNotification() {
    if (animating) return
    animating = true
    val durationTime = 300.0
    TranslateTransition().apply {
      duration = Duration.millis(durationTime)
      node = notification
      byY = -100.0
      interpolator = Interpolator.EASE_OUT
      play()
      setOnFinished {
        PauseTransition().apply {
          duration = Duration.seconds(2.0)
          play()
          setOnFinished {
            TranslateTransition().apply {
              duration = Duration.millis(durationTime)
              node = notification
              byY = 100.0
              interpolator = Interpolator.EASE_IN
              play()
              setOnFinished {
                animating = false
              }
            }
          }
        }
      }
    }
  }
  
  companion object {
    
    const val HIDDEN_PASSWORD_STUB = "•••••••••••••••••••"
  }
}
