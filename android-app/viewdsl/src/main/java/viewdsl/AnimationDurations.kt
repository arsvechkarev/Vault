package viewdsl

interface AnimationDurations {
  val SmallStubDelay: Long get() = 500L
  val VisibilityChange: Long get() = 300L
  val Rotation: Long get() = 1000L
}
