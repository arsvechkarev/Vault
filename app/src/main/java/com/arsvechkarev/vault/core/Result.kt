package com.arsvechkarev.vault.core

/**
 * Result class that facilitates working with different result types, such as Success,
 * Empty, Loading, Failure
 *
 * @param S type of success result
 *
 * @see ResultType
 * @see ResultConsumer
 */
class Result<S> private constructor(
  private val data: Any?,
  private val resultType: ResultType
) {
  
  /**
   * Accepts [consumerLambda] and applies that lambda to result type that this result currently is
   *
   * @see ResultConsumer
   */
  @Suppress("UNCHECKED_CAST")
  fun handle(consumerLambda: ResultConsumer<S>.() -> Unit) {
    val consumer = ResultConsumer<S>().apply(consumerLambda)
    when (resultType) {
      ResultType.LOADING -> consumer.onLoading.invoke()
      ResultType.EMPTY -> consumer.onEmpty.invoke()
      ResultType.SUCCESS -> consumer.onSuccess.invoke(data as S)
      ResultType.FAILURE -> consumer.onFailure.invoke(data as Throwable)
    }
  }
  
  val isLoading get() = resultType == ResultType.LOADING
  
  val isSuccess get() = resultType == ResultType.SUCCESS
  
  val isEmpty get() = resultType == ResultType.EMPTY
  
  val isFailure get() = resultType == ResultType.FAILURE
  
  companion object {
    
    fun <S> loading(): Result<S> = Result(null, ResultType.LOADING)
    
    fun <S> success(value: S): Result<S> = Result(value, ResultType.SUCCESS)
    
    fun <S> empty(): Result<S> = Result(null, ResultType.EMPTY)
    
    fun <S> success(value: Throwable): Result<S> = Result(value, ResultType.FAILURE)
  }
}

/**
 * Represents a result type that [Result] could have
 */
enum class ResultType {
  SUCCESS, FAILURE, LOADING, EMPTY
}

class ResultConsumer<S> {
  
  internal var onLoading: () -> Unit = {}
  internal var onEmpty: () -> Unit = {}
  internal var onSuccess: (S) -> Unit = {}
  internal var onFailure: (Throwable) -> Unit = {}
  
  fun onLoading(action: () -> Unit) {
    this.onLoading = action
  }
  
  fun onEmpty(action: () -> Unit) {
    this.onEmpty = action
  }
  
  fun onSuccess(action: (S) -> Unit) {
    this.onSuccess = action
  }
  
  fun onFailure(action: (Throwable) -> Unit) {
    this.onFailure = action
  }
}