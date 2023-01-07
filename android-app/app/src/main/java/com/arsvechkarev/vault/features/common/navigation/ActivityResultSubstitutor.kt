package com.arsvechkarev.vault.features.common.navigation

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.activity.result.contract.ActivityResultContracts.OpenDocumentTree
import androidx.fragment.app.Fragment

interface ActivityResultSubstitutor {
  
  fun wrapSelectFolderLauncher(
    fragment: Fragment,
    onSuccess: (Uri) -> Unit
  ): ActivityResultLauncher<Uri?>
  
  fun wrapCreateFileLauncher(
    fragment: Fragment,
    contentType: String,
    onSuccess: (Uri) -> Unit
  ): ActivityResultLauncher<String>
}

class RealActivityResultSubstitutor : ActivityResultSubstitutor {
  
  override fun wrapSelectFolderLauncher(
    fragment: Fragment,
    onSuccess: (Uri) -> Unit
  ): ActivityResultLauncher<Uri?> {
    return fragment.registerForActivityResult(OpenDocumentTree()) { it?.apply(onSuccess) }
  }
  
  override fun wrapCreateFileLauncher(
    fragment: Fragment,
    contentType: String,
    onSuccess: (Uri) -> Unit
  ): ActivityResultLauncher<String> {
    return fragment.registerForActivityResult(CreateDocument(contentType)) { it?.apply(onSuccess) }
  }
}
