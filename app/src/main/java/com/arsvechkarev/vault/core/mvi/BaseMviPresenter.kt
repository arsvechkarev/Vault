package com.arsvechkarev.vault.core.mvi

import androidx.annotation.CallSuper
import com.arsvechkarev.vault.core.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.MvpPresenter
import kotlin.reflect.KClass

/**
 * @param State state
 */
abstract class BaseMviPresenter<Action : Any, UserAction : Action, State>(
  private val userActionClass: KClass<UserAction>,
  protected val dispatchers: Dispatchers
) : MvpPresenter<MviView<State>>(), CoroutineScope {
  
  override val coroutineContext = dispatchers.Main + SupervisorJob()
  
  private var _state: State? = null
  val state: State get() = _state ?: getDefaultState()
  
  private var _states = MutableSharedFlow<State>(replay = 1)
  val states: SharedFlow<State>
    get() = _states
  
  private var _singleEvents = MutableSharedFlow<Any>(replay = 1)
  val singleEvents: SharedFlow<Any>
    get() = _singleEvents
  
  abstract fun getDefaultState(): State
  
  abstract fun reduce(action: Action): State
  
  abstract fun onSideEffect(action: UserAction)
  
  fun applyAction(action: Action) {
    _state = reduce(action)
    viewState.render(state)
    if (userActionClass.java.isInstance(action)) {
      @Suppress("UNCHECKED_CAST")
      onSideEffect(action as UserAction)
    }
  }
  
  protected suspend fun <T> onIoThread(block: suspend () -> T): T {
    return withContext(dispatchers.IO) { block() }
  }
  
  protected suspend fun <T> onBackgroundThread(block: suspend () -> T): T {
    return withContext(dispatchers.Default) { block() }
  }
  
  protected fun <T> Flow<T>.collectInPresenterScope(block: suspend (T) -> Unit) {
    launch { collect { block(it) } }
  }
  
  @CallSuper
  override fun onDestroy() {
    cancel()
  }
}