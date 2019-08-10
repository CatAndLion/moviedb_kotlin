package com.moviedb_kotlin.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.moviedb_kotlin.R
import com.moviedb_kotlin.adapters.ContentPagerAdapter
import io.reactivex.Observable
import io.reactivex.observables.ConnectableObservable

import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

interface SearchViewHolder {
    fun getSearchQuery(): Observable<String>
    fun openSearch()
    fun closeSearch(clearQuery: Boolean)
    fun setIconVisible(value: Boolean)
}

class MainActivity: AppCompatActivity(), SearchViewHolder {

    private lateinit var pageAdapter: ContentPagerAdapter

    private lateinit var searchTextObservable: ConnectableObservable<String>

    private var searchItem: MenuItem? = null

    val searchViewVisible: Boolean
        get() = titleFlipper.displayedChild == 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //setup action bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //setup tab layout
        pageAdapter = ContentPagerAdapter(supportFragmentManager, 0)
        contentPages.adapter = pageAdapter
        contentTabs.setupWithViewPager(contentPages)

        //setup query field
        searchTextObservable = Observable.create<String> { emmiter -> run {

            searchField.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    emmiter.onNext(newText!!)
                    return false
                }
            })
        }
        }.debounce(1000, TimeUnit.MILLISECONDS).publish()
        searchTextObservable.connect()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        searchItem = menu?.findItem(R.id.search_item)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when(item.itemId) {
                R.id.search_item -> {
                    if(searchViewVisible) {
                        closeSearch(true)
                    } else {
                        openSearch()
                    }
                }
            }
        }
        return false
    }

    override fun getSearchQuery(): Observable<String> {
        return searchTextObservable
    }

    override fun openSearch() {
        if(!searchViewVisible) {
            titleFlipper.showNext()
            searchItem?.setIcon(R.drawable.close_icon)
        }
    }

    override fun closeSearch(clearQuery: Boolean) {
        if(searchViewVisible) {
            if (clearQuery && !TextUtils.isEmpty(searchField.query)) {
                searchField.setQuery("", true)
            } else {
                titleFlipper.showNext()
                searchItem?.setIcon(R.drawable.search_icon)
            }
        }
    }

    override fun setIconVisible(value: Boolean) {
        searchItem?.isVisible = value
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setIconVisible(true)
        if(!TextUtils.isEmpty(searchField.query)) {
            openSearch()
        }
    }
}