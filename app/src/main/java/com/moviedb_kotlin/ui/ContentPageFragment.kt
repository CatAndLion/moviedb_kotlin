package com.moviedb_kotlin.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviedb_kotlin.R
import com.moviedb_kotlin.adapters.ContentListAdapter
import com.moviedb_kotlin.adapters.ItemClickListener
import com.moviedb_kotlin.viewmodels.Content
import com.moviedb_kotlin.viewmodels.ContentListViewModel
import com.moviedb_kotlin.viewmodels.ContentType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class ContentPageFragment: Fragment(), ItemClickListener {

    companion object {
        fun instantinate(type: ContentType): ContentPageFragment {
            return ContentPageFragment().apply {
                val args = Bundle()
                args.putInt("TYPE", type.ordinal)
                arguments = args
            }
        }
    }

    override fun itemClicked(view: View, item: Content) {
        searchHolder.setSearchActive(false)
        fragmentManager!!.beginTransaction()
            .replace(R.id.detailsPageContainer, ContentDetailsFragment.instantinate(item))
            .addToBackStack(null)
            .commit()
    }

    lateinit var viewModel: ContentListViewModel
    lateinit var adapter: ContentListAdapter
    lateinit var searchHolder: SearchViewHolder
    lateinit var type: ContentType

    var disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        type = ContentType.values()[arguments?.getInt("TYPE") ?: 0]
        viewModel = ViewModelProviders.of(this)[ContentListViewModel::class.java]
        viewModel.contentType = type
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // query changed
        if(activity is SearchViewHolder) {
            searchHolder = activity as SearchViewHolder
            disposables.add(searchHolder.getSearchQuery()
                .observeOn(AndroidSchedulers.mainThread())
                .map { it }
                .subscribe {query ->
                    if(!TextUtils.equals(query, viewModel.query)) {
                        // reload
                        adapter.clear()
                        viewModel.loadData(0, query)
                    }
                }
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.content_page, container, false)

        val list = view.findViewById<RecyclerView>(R.id.contentList)

        adapter = ContentListAdapter(context!!, 10, this)

        list.layoutManager = LinearLayoutManager(context!!)
        list.adapter = this.adapter

        // isLoading changed
        viewModel.isLoading.observe(this, Observer<Boolean> { value ->
            if(adapter.isLoading != value) {
                adapter.isLoading = value!!
                adapter.notifyDataSetChanged()
            }
        })

        // new data received
        disposables.add(viewModel.newData.subscribe {list ->
            adapter.total = viewModel.total
            adapter.addData(list)
            adapter.notifyDataSetChanged()
            //adapter.notifyItemRangeInserted(adapter.size - 1, list.size)
        })

        disposables.add(adapter.offsetObservable.subscribe {offset ->
            //load next page
            viewModel.loadData(offset)
        })

        if(!viewModel.isLoading.value!! && viewModel.total == 0) {
            //load first page
            viewModel.loadData()
        } else {
            adapter.addData(viewModel.data)
            adapter.notifyDataSetChanged()
        }

        return view
    }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }
}