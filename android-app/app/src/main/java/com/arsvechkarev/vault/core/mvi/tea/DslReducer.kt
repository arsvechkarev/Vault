package com.arsvechkarev.vault.core.mvi.tea

abstract class DslReducer<State : Any, Event : Any, Command : Any, News : Any>
  : Reducer<State, Event, Command, News> {
  
  private var _state: State? = null
  private var _event: Event? = null
  private var _commands: List<Command> = emptyList()
  private var _news: List<News> = emptyList()
  
  protected val state: State get() = checkNotNull(_state)
  
  protected abstract fun dslReduce(event: Event)
  
  protected fun state(supplier: State.() -> State) {
    _state = supplier(checkNotNull(_state))
  }
  
  protected fun commands(vararg commands: Command?) {
    _commands = commands.filterNotNull()
  }
  
  protected fun news(vararg news: News?) {
    _news = news.filterNotNull()
  }
  
  override fun reduce(state: State, event: Event): Update<State, Command, News> {
    _state = state
    _event = event
    dslReduce(event)
    val update = Update(
      state = this.state,
      commands = _commands,
      news = _news
    )
    _commands = emptyList()
    _news = emptyList()
    return update
  }
}
