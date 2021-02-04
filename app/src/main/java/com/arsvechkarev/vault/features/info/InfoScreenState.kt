package com.arsvechkarev.vault.features.info

enum class InfoScreenState {
  INITIAL,
  EDITING_NAME_OR_EMAIL,
  ERROR_EDITING_NAME,
  LOADING,
  PASSWORD_EDITING_DIALOG,
  SAVE_PASSWORD_DIALOG,
}