package com.arsvechkarev.vault.features.info

enum class InfoScreenState {
  INITIAL,
  EDITING_NAME_OR_USERNAME_OR_EMAIL,
  DELETING_DIALOG,
  LOADING,
  PASSWORD_EDITING_DIALOG,
  SAVE_PASSWORD_DIALOG,
}