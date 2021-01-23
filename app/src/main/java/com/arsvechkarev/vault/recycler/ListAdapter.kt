package com.arsvechkarev.vault.recycler

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.arsvechkarev.vault.core.AndroidThreader
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.recycler.CallbackType.ALWAYS_FALSE
import com.arsvechkarev.vault.recycler.CallbackType.APPENDED_LIST
import com.arsvechkarev.vault.recycler.CallbackType.TWO_LISTS
import kotlin.reflect.KClass

abstract class ListAdapter(
  private val callbackType: CallbackType = TWO_LISTS,
  private val threader: Threader = AndroidThreader,
  private var onReadyToLoadNextPage: (() -> Unit)? = null
) : RecyclerView.Adapter<ViewHolder>() {
  
  protected var recyclerView: RecyclerView? = null
    private set
  
  protected var data: MutableList<DifferentiableItem> = ArrayList()
  
  private val classesToViewTypes = HashMap<KClass<*>, Int>()
  private val delegatesSparseArray = SparseArrayCompat<ListAdapterDelegate<out DifferentiableItem>>()
  private val delegates = ArrayList<ListAdapterDelegate<out DifferentiableItem>>()
  
  constructor(
    vararg delegates: ListAdapterDelegate<out DifferentiableItem>,
    callbackType: CallbackType = TWO_LISTS,
    threader: Threader = AndroidThreader,
    onReadyToLoadNextPage: () -> Unit = {}
  ) : this(callbackType, threader, onReadyToLoadNextPage) {
    addDelegates(*delegates)
  }
  
  fun addItem(item: DifferentiableItem) {
    data.add(item)
    notifyItemInserted(data.lastIndex)
    recyclerView?.scrollToPosition(data.lastIndex)
  }
  
  fun addItems(list: List<DifferentiableItem>) {
    val oldSize = data.size
    data.addAll(list)
    applyChanges(AppendedListDiffCallbacks(data, oldSize))
  }
  
  fun changeListWithoutAnimation(list: List<DifferentiableItem>) {
    data = list as MutableList<DifferentiableItem>
    notifyDataSetChanged()
  }
  
  fun submitList(list: List<DifferentiableItem>) {
    val callback = when (callbackType) {
      APPENDED_LIST -> AppendedListDiffCallbacks(list, data.size)
      TWO_LISTS -> TwoListsDiffCallBack(data, list)
      ALWAYS_FALSE -> AlwaysFalseCallback(data, list)
    }
    data = list as MutableList<DifferentiableItem>
    applyChanges(callback)
  }
  
  protected fun addDelegates(vararg delegates: ListAdapterDelegate<out DifferentiableItem>) {
    this.delegates.addAll(delegates)
    delegates.forEachIndexed { i, delegate ->
      classesToViewTypes[delegate.modelClass] = i
      delegatesSparseArray.put(i, delegate)
    }
  }
  
  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    this.recyclerView = recyclerView
    delegates.forEach { it.onAttachedToRecyclerView(recyclerView) }
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val delegate = delegatesSparseArray[viewType] ?: error("No delegate for view type $viewType")
    return delegate.onCreateViewHolder(parent)
  }
  
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    if (position == data.size - 3) {
      onReadyToLoadNextPage?.invoke()
    }
    val adapterDelegate = delegatesSparseArray[getItemViewType(position)]
        ?: error("No delegate for position $position")
    adapterDelegate.onBindViewHolderRaw(holder, data[position])
  }
  
  override fun getItemViewType(position: Int): Int {
    return classesToViewTypes[data[position]::class] ?: error(
      "Can't find delegate for position: $position")
  }
  
  override fun getItemCount(): Int {
    return data.size
  }
  
  override fun onViewRecycled(holder: ViewHolder) {
    (holder as? DelegateViewHolder<*>)?.onViewRecycled()
  }
  
  override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
    delegates.forEach { it.onDetachedFromRecyclerView(recyclerView) }
    onReadyToLoadNextPage = null
  }
  
  private fun applyChanges(callback: DiffUtil.Callback) {
    threader.onBackground {
      val diffResult = DiffUtil.calculateDiff(callback)
      threader.onMainThread {
        diffResult.dispatchUpdatesTo(this)
      }
    }
  }
}