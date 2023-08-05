package com.arsvechkarev.vault.recycler

import android.view.View
import android.view.ViewGroup
import viewdsl.ViewBuilder
import viewdsl.inflate
import kotlin.reflect.KClass

inline fun <reified T : DifferentiableItem> delegate(
  block: DelegateBuilder<T>.() -> Unit
): DslListAdapterDelegate<T> {
  val builder = DelegateBuilder<T>().apply(block)
  return DslListAdapterDelegate(T::class, builder)
}

open class DelegateBuilder<T> {
  
  private var _layoutRes: Int = -1
  private var _simpleViewBuilder: ((parent: View) -> View)? = null
  private var _viewBuilder: (ViewBuilder.(View) -> View)? = null
  private var _viewHolderInitializer: DslDelegateViewHolder<T>.() -> Unit = { }
  private var _onBind: DslDelegateViewHolder<T>.() -> Unit = { }
  private var _onRecycled: ((itemView: View) -> Unit)? = null
  
  fun view(builder: (parent: View) -> View) {
    _simpleViewBuilder = builder
  }
  
  fun buildView(builder: ViewBuilder.(View) -> View) {
    _viewBuilder = builder
  }
  
  fun onInitViewHolder(function: DslDelegateViewHolder<T>.() -> Unit) {
    _viewHolderInitializer = function
  }
  
  fun onBind(function: DslDelegateViewHolder<T>.() -> Unit) {
    _onBind = function
  }
  
  internal fun createViewHolder(parent: ViewGroup): DslDelegateViewHolder<T> {
    val view = when {
      _layoutRes != -1 -> {
        parent.inflate(_layoutRes)
      }
      
      _simpleViewBuilder != null -> {
        _simpleViewBuilder!!.invoke(parent)
      }
      
      _viewBuilder != null -> {
        val builder = ViewBuilder(parent.context)
        _viewBuilder!!.invoke(builder, parent)
      }
      
      else -> throw IllegalArgumentException("Cannot create view holder")
    }
    return DslDelegateViewHolder(view, _onBind, _onRecycled).apply(_viewHolderInitializer)
  }
}

class DslListAdapterDelegate<T : DifferentiableItem>(
  klass: KClass<T>,
  private val delegateBuilder: DelegateBuilder<T>
) : ListAdapterDelegate<T>(klass) {
  
  override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<T> {
    return delegateBuilder.createViewHolder(parent)
  }
  
  override fun onBindViewHolder(holder: DelegateViewHolder<T>, item: T) {
    (holder as DslDelegateViewHolder)._item = item
    holder.bind(item)
  }
}

class DslAdapterDelegate<T : DisplayableItem>(
  klass: KClass<T>,
  private val delegateBuilder: DelegateBuilder<T>
) : AdapterDelegate<T>(klass) {
  
  override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<T> {
    return delegateBuilder.createViewHolder(parent)
  }
  
  override fun onBindViewHolder(holder: DelegateViewHolder<T>, item: T) {
    (holder as DslDelegateViewHolder)._item = item
    holder.bind(item)
  }
}

class DslDelegateViewHolder<T>(
  itemView: View,
  private val onBindFunction: DslDelegateViewHolder<T>.() -> Unit,
  private val _onRecycled: ((itemView: View) -> Unit)?
) : DelegateViewHolder<T>(itemView) {
  
  internal var _item: Any? = null
  
  val item: T
    get() = if (_item == null) {
      throw IllegalArgumentException("Item has not been set yet")
    } else {
      @Suppress("UNCHECKED_CAST")
      _item as T
    }
  
  override fun bind(item: T) = onBindFunction(this)
  
  override fun onViewRecycled() {
    _onRecycled?.invoke(itemView)
  }
}
