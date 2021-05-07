package config

/**
 * Represents duration values for animations
 */
object DurationsConfigurator {
  
  private var _shortVibration = 10L
  private var _short = 170L
  private var _default = 300L
  private var _medium = 500L
  private var _long = 800L
  private var _checkmark = 1300L
  
  val DurationShortVibration get() = _shortVibration
  val DurationShort get() = _short
  val DurationDefault get() = _default
  val DurationMedium get() = _medium
  val DurationLong get() = _long
  val DurationCheckmark get() = _checkmark
  
  fun setDurations(
    shortVibration: Long = DurationShortVibration,
    short: Long = DurationShort,
    default: Long = DurationDefault,
    medium: Long = DurationMedium,
    long: Long = DurationLong,
    checkmark: Long = DurationCheckmark,
  ) {
    _shortVibration = shortVibration
    _short = short
    _default = default
    _medium = medium
    _long = long
    _checkmark = checkmark
  }
  
  fun resetDurations() {
    _shortVibration = 0
    _short = 0
    _default = 0
    _medium = 0
    _long = 0
    _checkmark = 0
  }
}