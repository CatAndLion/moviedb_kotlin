package com.moviedb_kotlin.ui

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.moviedb_kotlin.R
import com.moviedb_kotlin.adapters.ContentPagerAdapter

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity: AppCompatActivity() {

    lateinit var pageAdapter: ContentPagerAdapter

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

        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}