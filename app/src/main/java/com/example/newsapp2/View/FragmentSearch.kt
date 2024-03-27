package com.example.newsapp2.View
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.HomePageNews.headlineapi
import com.example.newsapp2.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.lifecycle.lifecycleScope
import com.example.newsapp2.ViewModel.SearchNewsAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.launch

class FragmentSearch : Fragment() {
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var actualContentLayout: LinearLayout

    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var searchNewsAdapter: SearchNewsAdapter
    private lateinit var headlineApi: headlineapi // Corrected interface reference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        shimmerLayout = view.findViewById(R.id.shimmerLayout)
        actualContentLayout = view.findViewById(R.id.data_layout)

        searchRecyclerView = view.findViewById(R.id.search_recycler_view)
        searchView = view.findViewById(R.id.search_view)
        searchNewsAdapter = SearchNewsAdapter(requireContext())

        searchRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchNewsAdapter
        }
// Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
// Create an instance of the API interface
        headlineApi = retrofit.create(headlineapi::class.java) // Corrected reference
// Set up search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchNews(it) }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return view
    }
    private fun searchNews(query: String) {
        // Show shimmer layout while loading data
        shimmerLayout.visibility = View.VISIBLE

        // Using coroutine scope to launch a coroutine
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = headlineApi.searchForNews(query)
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    newsResponse?.let {
                        // Update RecyclerView with search results
                        searchNewsAdapter.submitList(it.articles)
                    }
                } else {
                    // Handle unsuccessful response
                }
            } catch (e: Exception) {
                // Handle network errors or other exceptions
            } finally {
                shimmerLayout.visibility = View.GONE
            }
        }
    }

}