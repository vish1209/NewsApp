package com.example.newsapp2.ViewModel

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.newsapp2.View.WebViewActivity
import com.example.newsapp2.Model.Article
import com.example.newsapp2.R
import com.example.newsapp2.Utils.DatabaseHelper

class HeadlineHorizontalAdapter(val context: Context, private val headLineData: List<Article>, private val viewPager2: ViewPager2)
    : RecyclerView.Adapter<HeadlineHorizontalAdapter.ImageViewHolder>(){
    private val databaseHelper = DatabaseHelper(context)

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageView : ImageView =itemView.findViewById(R.id.image_view_headLine_image_horizontalScroll)
        val titleTextView: TextView = itemView.findViewById(R.id.text_view_headLine_image_horizontalScroll)
        val bookmarkButton: ImageButton = itemView.findViewById(R.id.ImageButton_BookMark_headLine_image_horizontalScroll)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.horizontal_scroll_items_headline, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return headLineData.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val article = headLineData[position]
        holder.titleTextView.text = article.title ?: "Title Not Available"

        Glide.with(context)
            .load(article.urlToImage)
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, WebViewActivity::class.java)
            intent.putExtra("url", article.url)
            holder.itemView.context.startActivity(intent)
        }
        holder.bookmarkButton.setOnClickListener {
            val isCurrentlyBookmarked = databaseHelper.isArticleBookmarked(article.url ?: "")

            if (isCurrentlyBookmarked) {
                // Remove from bookmarks
                databaseHelper.deleteArticle(article.url ?: "")
                holder.bookmarkButton.setImageResource(R.drawable.icon_unbookmarked)
                Toast.makeText(context, "News unbookmarked", Toast.LENGTH_SHORT).show()
            } else {
                // Add to bookmarks
                databaseHelper.insertArticle(
                    title = article.title ?: "",
                    url = article.url ?: "",
                    urlToImage = article.urlToImage ?: ""
                )
                holder.bookmarkButton.setImageResource(R.drawable.icon_bookmarked)
                Toast.makeText(context, "News bookmarked", Toast.LENGTH_SHORT).show()
            }
        }

        // Scroll the ViewPager2 after a delay
        if (position == headLineData.size - 1) {
            holder.itemView.postDelayed({
                viewPager2.setCurrentItem(0, true) // Scroll to the first item
            }, 2000) // Delay in milliseconds (adjust as needed)
        }
    }

    private val runnable= Runnable{
        headLineData.addAll(headLineData)
        notifyDataSetChanged()
    }
}

private fun <E> List<E>.addAll(headLineData: List<E>) {

}
