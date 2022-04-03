package kotea.lifecycle

import androidx.core.app.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotea.Store
import kotea.ui.ContextResourcesProvider
import kotea.ui.UiStateMapper
import kotea.ui.map
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

/**
 * Создает подписку на `state` и `news`.
 * Подписка на state происходит между onStart/onStop
 * Подписка на news происходит между onResume/onPause
 *
 * Для activity рекоммендуется подписку делать в onCreate с использованием this, в качестве LifecycleOwner
 *
 * Для fragment рекоммендуется подписку делать в onViewCreated с использованием viewLifecycleOwner.
 * В случаях если у fragment onCreateView отдает null,
 * то подписку выполнять в onCreate и использовать this, в качестве LifecycleOwner
 */
fun <State : Any, News : Any> Store<State, *, News>.collect(
    lifecycleOwner: LifecycleOwner,
    stateCollector: ((State) -> Unit)? = null,
    newsCollector: ((News) -> Unit)? = null
): Unit = with(lifecycleOwner.lifecycleScope) {
    if (stateCollector != null) launchWhenStarted { state.collect(stateCollector::invoke) }
    if (newsCollector != null) launchWhenResumed { news.collect(newsCollector::invoke) }
}

/**
 * Перегрузка для случая, когда нужен UI mapping
 */
fun <State : Any, UiState : Any, News : Any> Store<State, *, News>.collect(
    activity: ComponentActivity,
    uiStateMapper: UiStateMapper<State, UiState>,
    stateCollector: ((UiState) -> Unit),
    newsCollector: ((News) -> Unit)? = null
): Unit = with(activity.lifecycleScope) {
    val provider = ContextResourcesProvider(activity)
    launchWhenStarted {
        state.map { uiStateMapper.map(provider, it) }.collect(stateCollector::invoke)
    }
    if (newsCollector != null) launchWhenResumed { news.collect(newsCollector::invoke) }
}

/**
 * Перегрузка для случая, когда нужен UI mapping
 */
fun <State : Any, UiState : Any, News : Any> Store<State, *, News>.collect(
    fragment: Fragment,
    uiStateMapper: UiStateMapper<State, UiState>,
    stateCollector: ((UiState) -> Unit),
    newsCollector: ((News) -> Unit)? = null
): Unit = with(fragment.lifecycleScope) {
    launchWhenStarted {
        val provider = ContextResourcesProvider(fragment.requireContext())
        state.map { uiStateMapper.map(provider, it) }.collect(stateCollector::invoke)
    }
    if (newsCollector != null) launchWhenResumed { news.collect(newsCollector::invoke) }
}
