package com.skdevelopment.newsapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

//Basically recylerView neeeds two things 1.arrayAdapter 2.viewHolder

//creating arrayAdapter extending from recylerView which takes parameter data as arraylist
class NewsAdapter( private val listener: itemClicked ) : RecyclerView.Adapter<NewsViewHolder>() {

    val items: ArrayList<News> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)  //this converts xml to view
            .inflate(R.layout.item_layout, parent, false)

            val viewH = NewsViewHolder(view)

            view.setOnClickListener {
                listener.onItemClicked(items[viewH.adapterPosition])
            }
        return viewH
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = items[position]
        holder.titleView.text = currentItem.title
        holder.sourceView.text = currentItem.source
        holder.dateView.text = currentItem.date
        Glide.with(holder.itemView.context).load(currentItem.imgUrl).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

     @SuppressLint("NotifyDataSetChanged")
     fun updateNews(updatedNews: ArrayList<News>) {
         items.clear()
         items.addAll(updatedNews)

         notifyDataSetChanged()
    }

}

//creating viewHolder
class NewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val titleView: TextView = itemView.findViewById(R.id.title)
    val imageView: ImageView = itemView.findViewById(R.id.image)
    val sourceView: TextView = itemView.findViewById(R.id.source)
    val dateView: TextView = itemView.findViewById(R.id.date)
}

//this is a callback which gives information to activity when an element is clicked
interface itemClicked {
    fun onItemClicked(item : News) {}
}