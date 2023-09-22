package com.arsvechkarev.vault.features.common.navigation

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.fragment.app.Fragment

interface ActivityResultWrapper {
  
  fun wrapCreateFileLauncher(
    fragment: Fragment,
    contentType: String,
    onSuccess: (Uri) -> Unit
  ): ActivityResultLauncher<String>
  
  fun wrapGetFileLauncher(
    fragment: Fragment,
    onSuccess: (Uri) -> Unit
  ): ActivityResultLauncher<String>
}

class RealActivityResultWrapper : ActivityResultWrapper {
  
  override fun wrapCreateFileLauncher(
    fragment: Fragment,
    contentType: String,
    onSuccess: (Uri) -> Unit
  ): ActivityResultLauncher<String> {
    return fragment.registerForActivityResult(CreateDocument(contentType)) { it?.apply(onSuccess) }
  }
  
  override fun wrapGetFileLauncher(
    fragment: Fragment,
    onSuccess: (Uri) -> Unit
  ): ActivityResultLauncher<String> {
    return fragment.registerForActivityResult(GetContent()) { it?.apply(onSuccess) }
  }
}
