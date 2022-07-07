package com.arsvechkarev.vault.core

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.arsvechkarev.vault.R
import viewdsl.getSystemService

class ClipboardImpl(private val context: Context) : Clipboard {
  
  override fun copyToClipboard(text: String) {
    val clipboardManager = context.getSystemService<ClipboardManager>()
    val clip = ClipData.newPlainText(context.getString(R.string.clipboard_label_password), text)
    clipboardManager.setPrimaryClip(clip)
  }
}
