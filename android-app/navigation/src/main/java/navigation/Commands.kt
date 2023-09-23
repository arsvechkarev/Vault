package navigation

interface Command

data class Forward(val screenInfo: ScreenInfo, val animate: Boolean) : Command

data class ForwardWithRemovalOf(val screenInfo: ScreenInfo, val screensCount: Int) : Command

data class BackTo(val screenInfo: ScreenInfo) : Command

data class SwitchToNewRoot(val screenInfo: ScreenInfo) : Command

object Back : Command
