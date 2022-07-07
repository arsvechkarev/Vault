package com.arsvechkarev.vault.core.extensions

import com.arsvechkarev.vault.core.DefaultDispatchersFacade
import com.arsvechkarev.vault.core.DispatchersFacade
import com.arsvechkarev.vault.core.mvi.MviView
import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import moxy.MvpDelegate
import moxy.MvpDelegateHolder
import moxy.MvpPresenter
import moxy.presenter.PresenterField
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <S : Any, E : Any, N : Any> MvpDelegateHolder.moxyStore(
  name: String = "store",
  factory: () -> TeaStore<S, E, N>
): ReadOnlyProperty<Any, TeaStore<S, E, N>> {
  val wrappedFactory: () -> PresenterStore<S, E, N> = {
    PresenterStore(factory(), DefaultDispatchersFacade)
  }
  return MoxyStoreDelegate(mvpDelegate, this::class.java.name + "." + name, wrappedFactory)
}

private class MoxyStoreDelegate<State : Any, Event : Any, News : Any>(
  delegate: MvpDelegate<*>,
  name: String,
  private val factory: () -> PresenterStore<State, Event, News>
) : ReadOnlyProperty<Any, TeaStore<State, Event, News>> {
  
  private var presenter: PresenterStore<State, Event, News>? = null
  
  init {
    val field = object : PresenterField<Any?>(name, null, null) {
      override fun providePresenter(delegated: Any?): MvpPresenter<*> = factory()
      override fun bind(container: Any?, presenter: MvpPresenter<*>) {
        @Suppress("UNCHECKED_CAST")
        this@MoxyStoreDelegate.presenter = presenter as PresenterStore<State, Event, News>
      }
    }
    delegate.registerExternalPresenterField(field)
  }
  
  override operator fun getValue(thisRef: Any, property: KProperty<*>): TeaStore<State, Event, News> {
    return presenter?.store ?: throw IllegalStateException(
      "Presenter can be accessed only after MvpDelegate.onCreate() call"
    )
  }
}

private class PresenterStore<State : Any, UiEvent : Any, News : Any>(
  val store: TeaStore<State, UiEvent, News>,
  dispatchersFacade: DispatchersFacade
) : MvpPresenter<MviView<State, News>>() {
  
  private val mainStoreScope = CoroutineScope(SupervisorJob())
  private val stateAndNewsScope = CoroutineScope(dispatchersFacade.Main)
  
  override fun onFirstViewAttach() {
    store.launch(mainStoreScope, DefaultDispatchersFacade)
  }
  
  @Suppress("UNCHECKED_CAST")
  override fun attachView(view: MviView<State, News>) {
    stateAndNewsScope.launch {
      store.state.collect { state -> (viewState as MviView<State, News>).render(state) }
    }
    stateAndNewsScope.launch {
      store.news.collect { news -> (viewState as MviView<State, News>).handleNews(news) }
    }
    super.attachView(view)
  }
  
  override fun detachView(view: MviView<State, News>) {
    stateAndNewsScope.coroutineContext.cancelChildren()
    super.detachView(view)
  }
  
  override fun onDestroy() {
    mainStoreScope.cancel()
  }
}
