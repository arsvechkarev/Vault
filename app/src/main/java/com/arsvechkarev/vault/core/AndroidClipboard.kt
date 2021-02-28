package com.arsvechkarev.vault.core

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.viewdsl.getSystemService

class AndroidClipboard(private val context: Context) : Clipboard {
  
  override fun copyPassword(password: String) {
    val clipboardManager = context.getSystemService<ClipboardManager>()
    val clip = ClipData.newPlainText(context.getString(R.string.clipboard_label_password), password)
    clipboardManager.setPrimaryClip(clip)
  }
}