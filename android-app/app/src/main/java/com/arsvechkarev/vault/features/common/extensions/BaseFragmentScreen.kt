package com.arsvechkarev.vault.features.common.extensions

import navigation.BaseFragmentScreen

fun BaseFragmentScreen.setStatusBarColor(color: Int) {
  requireActivity().window.statusBarColor = color
}
