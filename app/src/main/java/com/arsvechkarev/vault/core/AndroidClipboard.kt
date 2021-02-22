package com.arsvechkarev.vault.core

import android.content.ClipData
import android.content.ClipboardManager
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.viewdsl.ContextHolder
import com.arsvechkarev.vault.viewdsl.getSystemService

object AndroidClipboard : Clipboard {
  
  override fun copyPassword(password: String) {
    val context = ContextHolder.applicationContext
    val clipboardManager = context.getSystemService<ClipboardManager>()
    val clip = ClipData.newPlainText(context.getString(R.string.clipboard_label_password), password)
    clipboardManager.setPrimaryClip(clip)
  }
}