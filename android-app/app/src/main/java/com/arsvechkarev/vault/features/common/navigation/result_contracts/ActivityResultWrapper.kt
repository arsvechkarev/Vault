package com.arsvechkarev.vault.features.common.navigation.result_contracts

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
  
  fun wrapSelectFolderLauncher(
    fragment: Fragment,
    onSuccess: (Uri) -> Unit
  ): ActivityResultLauncher<Uri?>
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
  
  override fun wrapSelectFolderLauncher(
    fragment: Fragment,
    onSuccess: (Uri) -> Unit
  ): ActivityResultLauncher<Uri?> {
    return fragment.registerForActivityResult(SelectFolder()) { it?.apply(onSuccess) }
  }
}
