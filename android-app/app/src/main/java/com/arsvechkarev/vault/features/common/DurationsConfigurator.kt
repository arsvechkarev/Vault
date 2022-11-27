package com.arsvechkarev.vault.features.common

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
  private var _snackbar = 1000L
  
  val ShortVibration get() = _shortVibration
  val Short get() = _short
  val Default get() = _default
  val Medium get() = _medium
  val Long get() = _long
  val Checkmark get() = _checkmark
  val Snackbar get() = _snackbar
  val MenuOpening get() = _default
  
  fun setDurations(
    shortVibration: Long = ShortVibration,
    short: Long = Short,
    default: Long = Default,
    medium: Long = Medium,
    long: Long = Long,
    checkmark: Long = Checkmark,
    snackbar: Long = Snackbar,
  ) {
    _shortVibration = shortVibration
    _short = short
    _default = default
    _medium = medium
    _long = long
    _checkmark = checkmark
    _snackbar = snackbar
  }
  
  fun resetDurations() {
    _shortVibration = 0
    _short = 0
    _default = 0
    _medium = 0
    _long = 0
    _checkmark = 0
    _snackbar = 0
  }
}
