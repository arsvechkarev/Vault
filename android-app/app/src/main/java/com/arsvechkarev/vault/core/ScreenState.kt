package com.arsvechkarev.vault.core

/**
 * Result class that facilitates working with different result types, such as Success,
 * Empty, Loading
 *
 * @param S type of success result
 *
 * @see Type
 * @see ResultConsumer
 */
class ScreenState<S> private constructor(
  private val data: Any?,
  private val type: Type
) {
  
  val isEmpty get() = type == Type.EMPTY
  
  val isNotEmpty get() = !isEmpty
  
  @Suppress("UNCHECKED_CAST")
  fun <T> getItems(
    successItems: (data: S) -> List<T>,
    loadingItems: () -> List<T>,
    emptyItems: () -> List<T>,
  ): List<T> {
    return when (type) {
      Type.SUCCESS -> successItems(data as S)
      Type.LOADING -> loadingItems()
      Type.EMPTY -> emptyItems()
    }
  }
  
  companion object {
    
    fun <S> loading(): ScreenState<S> = ScreenState(null, Type.LOADING)
    
    fun <S> success(value: S): ScreenState<S> = ScreenState(value, Type.SUCCESS)
    
    fun <S> empty(): ScreenState<S> = ScreenState(null, Type.EMPTY)
  }
}

/**
 * Represents a result type that [ScreenState] could have
 */
private enum class Type {
  SUCCESS, LOADING, EMPTY
}
