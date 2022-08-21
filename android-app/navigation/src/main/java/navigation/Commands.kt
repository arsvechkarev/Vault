package navigation

interface Command

data class Forward(val screenInfo: ScreenInfo, val removeCurrentView: Boolean) : Command

data class Replace(val screenInfo: ScreenInfo) : Command

data class Back(val releaseCurrentScreen: Boolean) : Command

data class BackTo(val screenInfo: ScreenInfo, val releaseAllLeftScreens: Boolean) : Command

data class SwitchToNewRoot(val screenInfo: ScreenInfo) : Command