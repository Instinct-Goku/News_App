package com.skdevelopment.newsapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.skdevelopment.newsapp.NewsAdapter

class MainActivity : AppCompatActivity(), itemClicked {

    private lateinit var mAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //getting my recylerView
        val myList = findViewById<RecyclerView>(R.id.mylist)

        data()
        mAdapter = NewsAdapter(this)
        myList.adapter = mAdapter

        val chipGroup = findViewById<ChipGroup>(R.id.chip_group)

        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip: Chip? = group.findViewById(checkedId)
            chip?.let {chipView ->
                data(chip.text.toString())
            } ?: kotlin.run {
            }
        }
    }

    private fun data(category: String = "breaking-news") {
        val url = "https://gnews.io/api/v4/top-headlines?&country=in&topic=${category}&lang=en&token=YOUR-API-TOKEN"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val jsonArray = response.getJSONArray("articles")
                val newsArray = ArrayList<News>()

                for(i in 0 until jsonArray.length()){
                    val newsJsonObject = jsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getJSONObject("source").getString("name"),
                        newsJsonObject.getString("image"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("publishedAt").dropLast(10),
                    )
                    newsArray.add(news)
                }

                mAdapter.updateNews(newsArray)
            },
            { error ->
                Log.d("myLog", error.toString())
            }
        )

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }

    override fun onItemClicked(item: News) {
        val builder =  CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
}