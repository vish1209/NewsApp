package com.example.newsapp2.ViewModel
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp2.Model.Article
import com.example.newsapp2.R
import android.content.Context
import android.widget.ImageButton
import android.widget.Toast
import com.example.newsapp2.Utils.DatabaseHelper
import com.example.newsapp2.View.WebViewActivity


class SearchNewsAdapter(private val context: Context) : RecyclerView.Adapter<SearchNewsAdapter.NewsViewHolder>(){

    private var articles: List<Article> = listOf()
    private val databaseHelper = DatabaseHelper(context)
    private var isLoading = false



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_news_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {

            val article = articles[position]
            holder.bind(article)
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, WebViewActivity::class.java)
                intent.putExtra("url", article.url)
                context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return articles.size
    }

    fun submitList(newList: List<Article>) {
        articles = newList
        notifyDataSetChanged()
    }
    fun showShimmer() {
        isLoading = true
        notifyDataSetChanged()
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.tv_search_title)
        private val newsImageView: ImageView = itemView.findViewById(R.id.image_view_search_image)
        private val bookMarkButton: ImageButton = itemView.findViewById(R.id.button_save_search)


        fun bind(article: Article) {
            titleTextView.text = article.title

            Glide.with(itemView)
                .load(article.urlToImage)
                .placeholder(R.drawable.icon_app) // Placeholder image if loading fails
                .into(newsImageView)

            val isBookmarked = databaseHelper.isArticleBookmarked(article.url)
            val bookmarkIcon =
                if (isBookmarked) R.drawable.icon_bookmarked else R.drawable.icon_unbookmarked
            bookMarkButton.setImageResource(bookmarkIcon)

            bookMarkButton.setOnClickListener {
                val newState = !isBookmarked
                bookMarkButton.setImageResource(
                    if (newState) R.drawable.icon_bookmarked else R.drawable.icon_unbookmarked
                )
                val message = if (newState) "News bookmarked" else "News unbookmarked"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                if (isBookmarked) {
                    // Article is currently bookmarked, so unbookmark it
                    databaseHelper.deleteArticle(article.url ?: "")
                } else {
                    // Article is not bookmarked, so bookmark it
                    databaseHelper.insertArticle(
                        title = article.title ?: "",
                        url = article.url ?: "",
                        urlToImage = article.urlToImage ?: ""
                    )
                }
                // After handling bookmark state, update the bookmarked state
                notifyDataSetChanged()
            }
        }
    }
}