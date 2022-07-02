package com.arsvechkarev.vault.core.extensions

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import moxy.MvpDelegate
import moxy.MvpDelegateHolder
import moxy.MvpPresenter
import moxy.MvpView
import moxy.presenter.PresenterField
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <S : Any, E : Any, N : Any> MvpDelegateHolder.moxyStore(
  name: String = "store",
  factory: () -> TeaStore<S, E, N>
): ReadOnlyProperty<Any, TeaStore<S, E, N>> {
  val wrappedFactory: () -> PresenterStore<S, E, N> = { PresenterStore(factory()) }
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
  val store: TeaStore<State, UiEvent, News>
) : MvpPresenter<MvpView>()
