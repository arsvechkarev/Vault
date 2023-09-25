package com.arsvechkarev.vault.features.common

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.arsvechkarev.vault.core.views.drawables.LetterInCircleDrawable
import viewdsl.retrieveDrawable

private const val AMAZON = "amazon"
private const val GOOGLE = "google"
private const val FACEBOOK = "facebook"
private const val MICROSOFT = "microsoft"
private const val NETFLIX = "netflix"
private const val INSTAGRAM = "instagram"
private const val SNAPCHAT = "snapchat"
private const val TELEGRAM = "telegram"
private const val TWITTER = "twitter"
private const val SPOTIFY = "spotify"
private const val VK = "vk"
private const val WHATSAPP = "whatsapp"
private const val GITHUB = "github"

fun ImageView.setIconForTitle(title: String) {
  val text = title.trim()
  if (text.isBlank()) {
    setImageDrawable(null) // Text is blank, showing no icon
    return
  }
  val predefinedIcon = findPredefinedIcon(text)
  if (predefinedIcon != null) {
    setImageDrawable(predefinedIcon)
  } else {
    // No predefined icon found, setting LetterInCircleImage
    val letter = title[0].toString()
    (this.drawable as? LetterInCircleDrawable)?.setLetter(letter) ?: run {
      setImageDrawable(LetterInCircleDrawable(letter))
    }
  }
}

// TODO (9/22/23): Getting icons from network?
private fun ImageView.findPredefinedIcon(text: String): Drawable? {
  val drawable: (Int) -> Drawable = { context.retrieveDrawable(it) }
  return null
}

private infix fun String.has(text: String): Boolean {
  return this.contains(text, ignoreCase = true)
}
