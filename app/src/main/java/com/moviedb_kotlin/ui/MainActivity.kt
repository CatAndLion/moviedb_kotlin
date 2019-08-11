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
    fun setSearchVisible(visible: Boolean)
    fun setSearchActive(enabled: Boolean)
}

class MainActivity: AppCompatActivity(), SearchViewHolder {

    private lateinit var pageAdapter: ContentPagerAdapter

    private lateinit var searchTextObservable: ConnectableObservable<String>

    private var searchItem: MenuItem? = null

    var searchEnabled: Boolean = true
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
                    if(searchViewVisible && !TextUtils.isEmpty(searchField.query)) {
                        searchField.setQuery("", true)
                    } else {
                        setSearchVisible(!searchViewVisible)
                    }
                }
            }
        }
        return false
    }

    override fun getSearchQuery(): Observable<String> {
        return searchTextObservable
    }

    override fun setSearchVisible(visible: Boolean) {
        if(searchEnabled) {
            if (searchViewVisible != visible) {
                searchItem?.setIcon(if(visible) R.drawable.close_icon else R.drawable.search_icon)
                titleFlipper.showNext()
            }
        }
    }

    override fun setSearchActive(enabled: Boolean) {
        if(!enabled) {
            setSearchVisible(false)
        }
        searchEnabled = enabled
        searchItem?.isVisible = enabled
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setSearchActive(true)
        if(!TextUtils.isEmpty(searchField.query)) {
            setSearchVisible(true)
        }
    }
}