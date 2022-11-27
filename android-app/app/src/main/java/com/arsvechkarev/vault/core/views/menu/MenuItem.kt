package com.arsvechkarev.vault.core.views.menu

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class MenuItem(
  @DrawableRes val iconRes: Int,
  @StringRes val titleRes: Int,
  val onClick: () -> Unit,
)
