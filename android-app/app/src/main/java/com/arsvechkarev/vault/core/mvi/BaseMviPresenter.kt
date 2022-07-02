package com.arsvechkarev.vault.core.mvi

import androidx.annotation.CallSuper
import com.arsvechkarev.vault.core.DispatchersFacade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.MvpPresenter
import kotlin.reflect.KClass

/**
 * Base presenter for mvi architecture
 *
 * [Action] action is basically anything that can happen in the program. It could represent program events,
 * such as loaded data, error from server etc. It also can be user input, such as button clicks, edit text
 * inputs, etc., Although for user inputs you should [UserAction], which is a subclass of [Action].
 *
 * [State] is a state of a program: Loaded data, dialogs, whether something is checked or selected, etc.
 * What happens is actions are applied to current state, from which new state might emerge and become
 * current state. This way the we always know what current state is and we control its transformations
 *
 * Whenever [UserAction] happens, state is transformed and method [onSideEffect] is called. This is
 * our chance to react to user actions. Here we can initiate data loading, show dialogs, react to button
 * clicks, etc.
 */
abstract class BaseMviPresenter<Action : Any, UserAction : Action, State>(
  private val userActionClass: KClass<UserAction>,
  protected val dispatchers: DispatchersFacade
) : MvpPresenter<MviView<State>>(), CoroutineScope {
  
  override val coroutineContext = dispatchers.Main + SupervisorJob()
  
  private var _state: State? = null
  val state: State get() = _state ?: getDefaultState()
  
  /**
   * Returns default state for this presenter
   */
  abstract fun getDefaultState(): State
  
  /**
   * Processes [action] and applies it to state if necessary
   */
  abstract fun reduce(action: Action): State
  
  /**
   * Called when [applyAction] was called with an instance of [UserAction]
   */
  open fun onSideEffect(action: UserAction) = Unit
  
  /**
   * Submits [action] to processing
   */
  fun applyAction(action: Action) {
    val oldState = _state
    _state = reduce(action)
    if (oldState != state) {
      viewState.render(state)
    }
    if (userActionClass.java.isInstance(action)) {
      @Suppress("UNCHECKED_CAST")
      onSideEffect(action as UserAction)
    }
  }
  
  fun showSingleEvent(event: Any) {
    launch {
      viewState.renderSingleEvent(event)
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