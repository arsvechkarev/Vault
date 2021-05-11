package com.arsvechkarev.vault.core

import androidx.appcompat.app.AppCompatActivity

interface ActivityProvider {
  
  val activity: AppCompatActivity?
  
  fun requireActivity(): AppCompatActivity {
    return activity ?: error("Activity is null")
  }
}