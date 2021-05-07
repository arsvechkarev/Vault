package navigation

import android.os.Bundle
import com.github.terrakok.cicerone.Navigator

interface ExtendedNavigator : Navigator {
  
  /**
   * Handles going back in stack. Returns true, if there was any screens in stack to be removed
   * (and removes screen), or if current screen handled back interaction. Returns false otherwise
   */
  fun handleGoBack(): Boolean
  
  /**
   * Saves navigation state in [bundle]
   */
  fun onSaveInstanceState(bundle: Bundle)
  
  /**
   * Restores navigation state from [bundle] if needed
   */
  fun onRestoreInstanceState(bundle: Bundle?)
  
  /**
   * Releases screens. You should call this from Activity.onDestroy()
   */
  fun releaseScreens()
}