package viewdsl

@Suppress("ClassName")
sealed class Size {
  
  object MATCH_PARENT : Size()
  object WRAP_CONTENT : Size()
  class IntSize(val size: Int) : Size()
  class Dimen(val dimenRes: Int) : Size()
  
  companion object {
    
    val ZERO = IntSize(0)
    val MatchParent get() = MATCH_PARENT
    val WrapContent get() = WRAP_CONTENT
  }
}
