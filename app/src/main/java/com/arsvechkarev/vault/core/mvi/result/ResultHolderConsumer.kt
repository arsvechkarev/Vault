package com.arsvechkarev.vault.core.mvi.result

class ResultHolderConsumer<S> {
  
  internal var onLoading: () -> Unit = {}
  internal var onEmpty: () -> Unit = {}
  internal var onSuccess: (S) -> Unit = {}
  internal var onFailure: (Throwable) -> Unit = {}
  
  fun onLoading(action: () -> Unit) {
    this.onLoading = action
  }
  
  fun onEmpty(action: () -> Unit) {
    this.onEmpty = action
  }
  
  fun onSuccess(action: (S) -> Unit) {
    this.onSuccess = action
  }
  
  fun onFailure(action: (Throwable) -> Unit) {
    this.onFailure = action
  }
}