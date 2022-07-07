package viewdsl

typealias Style<T> = T.() -> Unit

infix fun <T> Style<T>.thenApply(other: Style<T>): Style<T> {
  return CompositeStyle(this, other)
}

private class CompositeStyle<T>(
  private val first: Style<T>,
  private val second: Style<T>
) : (T) -> Unit {
  override fun invoke(view: T) {
    view.apply(first).apply(second)
  }
}
