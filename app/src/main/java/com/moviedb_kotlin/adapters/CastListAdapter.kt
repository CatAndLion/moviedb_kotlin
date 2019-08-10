package com.moviedb_kotlin.adapters

import android.app.Service
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moviedb_kotlin.R
import com.moviedb_kotlin.utils.GlideAppModule
import com.moviedb_kotlin.viewmodels.Person

import kotlinx.android.synthetic.main.person_list_item.view.*

class CastListAdapter(context: Context): RecyclerView.Adapter<CastListAdapter.CastViewHolder>() {

    val inflater = context.getSystemService(Service.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val data: MutableList<Person> = mutableListOf()

    fun addData(data: List<Person>) {
        this.data.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder(inflater.inflate(R.layout.person_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.fill(data[position])
    }

    class CastViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun fill(item: Person) {

            itemView.name.text = item.name
            itemView.info.text = item.character

            if(!TextUtils.isEmpty(item.posterPath)) {
                GlideAppModule.getRequest(itemView, GlideAppModule.CacheOptions.Memory)
                    .load(item.posterPath)
                    .error(R.drawable.person_stub)
                    .into(itemView.profileImage)
            }
        }
    }
}