package viewdsl

interface AnimationDurations {
  val VisibilityChangeFast: Long get() = 150L
  val VisibilityChange: Long get() = 300L
  val Rotation: Long get() = 1000L
}
