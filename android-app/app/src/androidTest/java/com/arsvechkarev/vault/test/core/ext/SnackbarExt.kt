package com.arsvechkarev.vault.test.core.ext

import android.os.SystemClock
import com.arsvechkarev.vault.features.common.Durations


fun waitForSnackbarToHide() {
  SystemClock.sleep(Durations.Snackbar * 3)
}