package com.arsvechkarev.vault.test.core.stub

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import com.arsvechkarev.vault.features.common.navigation.ActivityResultWrapper

class StubActivityResultWrapper(
  private val stubSelectedFolderUri: String = "",
  private val stubCreatedFileUri: String = "",
  private val stubGetFileUri: String = "",
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
  
  override fun wrapGetFileLauncher(
    fragment: Fragment,
    onSuccess: (Uri) -> Unit
  ): ActivityResultLauncher<String> {
    return fragment.registerForActivityResult(
      GetContent(),
      getReturningUriTestRegistry(stubGetFileUri)
    ) {
      it?.apply(onSuccess)
    }
  }
}
