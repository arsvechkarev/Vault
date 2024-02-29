package com.arsvechkarev.vault.test.core.di.stubs

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.activity.result.contract.ActivityResultContracts.OpenDocumentTree
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import com.arsvechkarev.vault.features.common.navigation.result_contracts.ActivityResultWrapper

class StubActivityResultWrapper(
  private val stubCreatedFileUri: String = "",
  private val stubSelectPasswordsFileUri: String = "",
  private val stubSelectKeyFileUri: String = "",
  private val stubSelectedFolderUri: String = "",
) : ActivityResultWrapper {
  
  private fun getReturningUriTestRegistry(
    uri: String
  ): ActivityResultRegistry {
    return object : ActivityResultRegistry() {
      override fun <I, O> onLaunch(
        requestCode: Int,
        contract: ActivityResultContract<I, O>,
        input: I,
        options: ActivityOptionsCompat?
      ) {
        dispatchResult(requestCode, Uri.parse(uri))
      }
    }
  }
  
  override fun wrapCreateFileLauncher(
    fragment: Fragment,
    contentType: String,
    onSuccess: (Uri) -> Unit
  ): ActivityResultLauncher<String> {
    return fragment.registerForActivityResult(
      CreateDocument(contentType),
      getReturningUriTestRegistry(stubCreatedFileUri)
    ) {
      it?.apply(onSuccess)
    }
  }
  
  override fun wrapSelectPasswordsFileLauncher(
    fragment: Fragment,
    onSuccess: (Uri) -> Unit
  ): ActivityResultLauncher<String> {
    return fragment.registerForActivityResult(
      GetContent(),
      getReturningUriTestRegistry(stubSelectPasswordsFileUri)
    ) {
      it?.apply(onSuccess)
    }
  }
  
  override fun wrapSelectKeyFileLauncher(
    fragment: Fragment,
    onSuccess: (Uri) -> Unit
  ): ActivityResultLauncher<String> {
    return fragment.registerForActivityResult(
      GetContent(),
      getReturningUriTestRegistry(stubSelectKeyFileUri)
    ) {
      it?.apply(onSuccess)
    }
  }
  
  override fun wrapSelectFolderLauncher(
    fragment: Fragment,
    onSuccess: (Uri) -> Unit
  ): ActivityResultLauncher<Uri?> {
    return fragment.registerForActivityResult(
      OpenDocumentTree(),
      getReturningUriTestRegistry(stubSelectedFolderUri)
    ) {
      it?.apply(onSuccess)
    }
  }
}
