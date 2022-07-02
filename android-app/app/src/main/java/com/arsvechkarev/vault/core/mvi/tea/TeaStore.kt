package com.arsvechkarev.vault.core.mvi.tea

import com.arsvechkarev.vault.core.DispatchersFacade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface TeaStore<out State : Any, in UiEvent : Any, News : Any> {
  
  val stateFlow: StateFlow<State>
  
  val news: SharedFlow<News>
  
  fun launch(coroutineScope: CoroutineScope, dispatchersFacade: DispatchersFacade)
  
  fun dispatch(event: UiEvent)
}