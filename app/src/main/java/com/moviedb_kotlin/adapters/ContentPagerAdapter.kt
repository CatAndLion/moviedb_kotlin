package com.moviedb_kotlin.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.moviedb_kotlin.ui.ContentPageFragment
import com.moviedb_kotlin.viewmodels.ContentType

class ContentPagerAdapter(manager: FragmentManager, behaviour: Int):
    FragmentPagerAdapter(manager, behaviour) {

    override fun getItem(position: Int): Fragment {
        val type = if(position == 0) ContentType.Movie else ContentType.TvShow
        return ContentPageFragment(type)
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Movies"
            1 -> "Tv Shows"
            else -> ""
        }
    }
}