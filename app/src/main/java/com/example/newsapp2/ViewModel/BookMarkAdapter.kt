package com.example.newsapp2.ViewModel

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp2.Model.BookMarkModel.ArticleModel
import com.example.newsapp2.Utils.DatabaseHelper
import com.example.newsapp2.View.WebViewActivity
import com.example.newsapp2.R

class BookMarkAdapter(private val databaseHelper: DatabaseHelper, private val listener: OnBookmarkClickListener): RecyclerView.Adapter<BookMarkAdapter.BookMarkViewHolder>() {
    private var articles: List<ArticleModel> = listOf()

    interface OnBookmarkClickListener {
        fun onBookmarkClicked(article: ArticleModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bookmark_item, parent, false)
        return BookMarkViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookMarkViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)

        // Check if the article is bookmarked
        val isBookmarked = databaseHelper.isArticleBookmarked(article.url)

        // Set bookmark button state based on isBookmarked
        val bookmarkIcon =
            if (isBookmarked) R.drawable.icon_bookmarked else R.drawable.icon_unbookmarked
        holder.bookmarkButton.setImageResource(bookmarkIcon)

        holder.bookmarkButton.setOnClickListener {
            val newState = !isBookmarked
            holder.bookmarkButton.setImageResource(
                if (newState) R.drawable.icon_bookmarked else R.drawable.icon_unbookmarked
            )
            val message = if (newState) "News bookmarked" else "News unbookmarked"
            Toast.makeText(holder.itemView.context, message, Toast.LENGTH_SHORT).show()

            if (newState) {
                // Check if the article is already bookmarked
                if (!isBookmarked) {
                    databaseHelper.insertArticle(
                        title = article.title ?: "",
                        url = article.url ?: "",
                        urlToImage = article.urlToImage ?: ""
                    )
                }
            } else {
                // Delete the article from the database if it was bookmarked and now unbookmarked
                databaseHelper.deleteArticle(article.url)
            }
            listener.onBookmarkClicked(article)
        }
        holder.itemView.setOnClickListener {
            // Redirect the user to the WebViewActivity when the item is clicked
            val intent = Intent(holder.itemView.context, WebViewActivity::class.java)
            intent.putExtra("url", article.url)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun submitList(newList: List<ArticleModel>) {
        articles = newList
        notifyDataSetChanged()
    }

    inner class BookMarkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.tv_BookMark_title)
        private val imageImageView: ImageView=itemView.findViewById(R.id.image_view_BookMark_image)
        val bookmarkButton: ImageView = itemView.findViewById(R.id.button_BookMarked_BookMark_page)

        fun bind(article: ArticleModel) {
            titleTextView.text = article.title
            Glide.with(itemView.context)
                .load(article.urlToImage)
                .placeholder(R.drawable.icon_app) // Placeholder image if loading fails
                .into(imageImageView)


        }

    }
}