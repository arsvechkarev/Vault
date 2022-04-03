package navigation

import com.github.terrakok.cicerone.BaseRouter

class Router : BaseRouter() {

    fun goForward(screenInfo: ScreenInfo, removeCurrentView: Boolean = false) {
        executeCommands(Forward(screenInfo, removeCurrentView))
    }

    fun replace(screenInfo: ScreenInfo) {
        executeCommands(Replace(screenInfo))
    }

    fun goBack(releaseCurrentScreen: Boolean = true) {
        executeCommands(Back(releaseCurrentScreen))
    }

    fun goBackTo(screenInfo: ScreenInfo, releaseAllLeftScreens: Boolean = true) {
        executeCommands(BackTo(screenInfo, releaseAllLeftScreens))
    }

    fun switchToNewRoot(screenInfo: ScreenInfo) {
        executeCommands(SwitchToNewRoot(screenInfo))
    }
}