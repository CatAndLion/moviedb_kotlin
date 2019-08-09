package com.moviedb_kotlin.adapters

import android.app.Service
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moviedb_kotlin.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlin.math.max

abstract class PagedListAdapter<D, in H>(context: Context, limit: Int): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val inflater = context.getSystemService(Service.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val data: MutableList<D> = mutableListOf()

    var limit: Int = limit
        set(value) { field = max(0, value) }

    val size: Int
        get() = data.size

    var total: Int = 0
        set(value) { field = max(0, value) }

    var isLoading: Boolean = false
        set(value) { field = value }

    fun addData(data: List<D>) {
        this.data.addAll(data)
    }

    private val offsetController = PublishSubject.create<Int>()

    val offsetObservable = offsetController
        .observeOn(AndroidSchedulers.mainThread())
        .distinctUntilChanged()

    final override fun getItemCount(): Int {
        return if(isLoading && data.isEmpty()) 1 else
            if(size < total) size + 1 else size
    }

    final override fun getItemViewType(position: Int): Int {
        return if(isLoading && data.isEmpty()) 2 else
            if(size < total && position >= size) 1 else 0
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(viewType) {
            2 -> DefaultViewHolder(inflater.inflate(R.layout.loading_item, parent, false))
            1 -> LoadingItemViewHolder(inflater.inflate(R.layout.loading_list_item, parent, false))
            else -> createItemViewHolder(parent)
        }
    }

    final override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is LoadingItemViewHolder) {
            offsetController.onNext(size)
        } else if(position < size) {
            bindItemViewHolder(holder as H, data[position])
        }
    }

    abstract fun createItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    abstract fun bindItemViewHolder(viewHolder: H, item: D)

    fun clear() {
        this.total = 0
        this.data.clear()
    }

    class DefaultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class LoadingItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}