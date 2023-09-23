package com.arsvechkarev.vault.features.common.domain

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import viewdsl.getSystemService

interface Clipboard {
  fun copyToClipboard(labelRes: Int, text: String)
}

class ClipboardImpl(private val context: Context) : Clipboard {
  
  override fun copyToClipboard(labelRes: Int, text: String) {
    val clipboardManager = context.getSystemService<ClipboardManager>()
    val clip = ClipData.newPlainText(context.getString(labelRes), text)
    clipboardManager.setPrimaryClip(clip)
  }
}

