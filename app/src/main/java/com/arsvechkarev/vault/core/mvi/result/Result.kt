package com.arsvechkarev.vault.core.mvi.result

class Result<S> private constructor(
  private val data: Any?,
  private val resultType: ResultType
) {
  
  @Suppress("UNCHECKED_CAST")
  fun handle(consumerLambda: ResultHolderConsumer<S>.() -> Unit) {
    val consumer = ResultHolderConsumer<S>().apply(consumerLambda)
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