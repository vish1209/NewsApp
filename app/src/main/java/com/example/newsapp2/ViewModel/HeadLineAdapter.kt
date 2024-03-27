package com.example.newsapp2.ViewModel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp2.Model.Article
import com.example.newsapp2.Utils.DatabaseHelper
import com.example.newsapp2.R

class HeadLineAdapter(val context: Context, val headLineData: List<Article>, private val headlineDetailInterface: HeadlineDetailInterface): RecyclerView.Adapter<HeadLineAdapter.ViewHolder>(){


    private val databaseHelper = DatabaseHelper(context)


    var default_url ="https://www.shutterstock.com/image-vector/breaking-news-isolated-vector-icon-600nw-1670421838.jpg"
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view_headLine_image)
        val title: TextView = itemView.findViewById(R.id.tv_headline_title)
        val publishDate: TextView = itemView.findViewById(R.id.tv_publish_date)
        val bookMarkButton: ImageButton =itemView.findViewById(R.id.button_BookMark_headline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView=LayoutInflater.from(context).inflate(R.layout.headline_row_items, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return headLineData.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = headLineData[position]

        article?.let {
            holder.title.text = it.title ?: "Title Not Available"
            holder.publishDate.text = it.publishedAt ?: "Date Not Available"

            Glide.with(context)
                .load(it.urlToImage ?: R.drawable.icon_app)
                .into(holder.imageView)

            // Check if the article is bookmarked
            val isBookmarked = databaseHelper.isArticleBookmarked(article.url ?: "")

            // Set bookmark button state based on isBookmarked
            val bookmarkIcon =
                if (isBookmarked) R.drawable.icon_bookmarked else R.drawable.icon_unbookmarked
            holder.bookMarkButton.setImageResource(bookmarkIcon)

            holder.itemView.setOnClickListener {
                headlineDetailInterface.getDetails(
                    article.title ?: "Missing Title",
                    article.urlToImage ?: default_url,
                    article.content ?: "Missing Content",
                    article.url ?: "https://newsapi.org/docs/authentication"
                )
            }

            holder.bookMarkButton.setOnClickListener {
                val isCurrentlyBookmarked = databaseHelper.isArticleBookmarked(article.url ?: "")

                if (isCurrentlyBookmarked) {
                    // Remove from bookmarks
                    databaseHelper.deleteArticle(article.url ?: "")
                    holder.bookMarkButton.setImageResource(R.drawable.icon_unbookmarked)
                    Toast.makeText(context, "News unbookmarked", Toast.LENGTH_SHORT).show()
                } else {
                    // Add to bookmarks
                    databaseHelper.insertArticle(
                        title = article.title ?: "",
                        url = article.url ?: "",
                        urlToImage = article.urlToImage ?: ""
                    )
                    holder.bookMarkButton.setImageResource(R.drawable.icon_bookmarked)
                    Toast.makeText(context, "News bookmarked", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}