package com.arsvechkarev.core.navigation

import android.content.Context
import android.os.Bundle
import android.view.View

class ScreenMetadata {
  var _arguments: Bundle? = null
  var _view: View? = null
  var _context: Context? = null
  var removeOnExit: Boolean = false
}