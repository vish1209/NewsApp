package com.example.newsapp2.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp2.Model.BookMarkModel.ArticleModel
import com.example.newsapp2.Utils.DatabaseHelper
import com.example.newsapp2.R
import com.example.newsapp2.ViewModel.BookMarkAdapter

class FragmentBookMark : Fragment() , BookMarkAdapter.OnBookmarkClickListener{

    private lateinit var bookMarkRecyclerView: RecyclerView
    private lateinit var bookMarkAdapter: BookMarkAdapter
    private lateinit var databaseHelper: DatabaseHelper



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book_mark, container, false)

        databaseHelper = DatabaseHelper(requireContext())

        bookMarkRecyclerView = view.findViewById(R.id.bookMark_recycler_view)
        bookMarkAdapter = BookMarkAdapter(databaseHelper, this)

        bookMarkRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = bookMarkAdapter
        }

        // Load data from database and set it to the adapter
        val articles = databaseHelper.getAllArticles()
        bookMarkAdapter.submitList(articles)

        return view
    }
    override fun onBookmarkClicked(article: ArticleModel) {
        val articles = databaseHelper.getAllArticles()
        bookMarkAdapter.submitList(articles)
    }
}