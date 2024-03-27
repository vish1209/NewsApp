package com.example.newsapp2.View

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp2.ViewModel.HeadLineAdapter
import com.example.newsapp2.ViewModel.HeadlineDetailInterface
import com.example.newsapp.HomePageNews.headlineapi
import com.example.newsapp2.Model.HeadLineData
import com.example.newsapp2.R
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.newsapp2.ViewModel.HeadlineHorizontalAdapter
import com.example.newsapp2.ViewModel.SearchNewsAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import kotlin.math.abs

class FragmentHomePage : Fragment(), HeadlineDetailInterface {

    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var shimmerLayoutSmall :ShimmerFrameLayout
    private lateinit var actualContentLayout: LinearLayout

    private val baseUrl="https://newsapi.org/"

    private lateinit var headlineApi: headlineapi



    lateinit var myAdapter: HeadLineAdapter
    private lateinit var horizontalAdapter: HeadlineHorizontalAdapter
    private lateinit var searchNewsAdapter: SearchNewsAdapter

    lateinit var linearLayoutManager: LinearLayoutManager

    lateinit var recycler_view_headLines: RecyclerView
    lateinit var recycler_view_headLines_horizontalScroll: ViewPager2
    lateinit var handler: Handler

    private lateinit var searchRecyclerView: RecyclerView

    lateinit var All_news_textView:TextView
    lateinit var Business_textView:TextView
    lateinit var Politics_textView:TextView
    lateinit var Tech_textView:TextView
    lateinit var Science_textView:TextView
    lateinit var Local_news_textView:TextView
    lateinit var tv_latest_news:TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

        shimmerLayout = view.findViewById(R.id.shimmerLayout)
        actualContentLayout = view.findViewById(R.id.data_layout)
        shimmerLayoutSmall=view.findViewById(R.id.shimmerLayoutsmall)

        recycler_view_headLines = view.findViewById(R.id.recycler_view_headLines)
        recycler_view_headLines_horizontalScroll= view.findViewById(R.id.recycler_view_headLines_scroll_image)
        handler=Handler(Looper.myLooper()!!)

        All_news_textView= view.findViewById(R.id.All_news_textView)
        Business_textView= view.findViewById(R.id.Business_textView)
        Politics_textView= view.findViewById(R.id.Politics_textView)
        Tech_textView= view.findViewById(R.id.Tech_textView)
        Science_textView= view.findViewById(R.id.Science_textView)
        Local_news_textView= view.findViewById(R.id.Local_news_textView)
        tv_latest_news= view.findViewById(R.id.tv_latest_news)

        recycler_view_headLines.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(requireContext())
        recycler_view_headLines.layoutManager = linearLayoutManager


        searchRecyclerView= view.findViewById(R.id.recycler_view_headLines)
        searchNewsAdapter = SearchNewsAdapter(requireContext())

        getHorizontalHeadLineData()
        getHeadLineData()
        setuptransformer()
        recycler_view_headLines_horizontalScroll.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 3000)
            }
        })

        All_news_textView.setOnClickListener{
            showSearchResult("a")
            setbackground(All_news_textView)

        }
        Business_textView.setOnClickListener{
            showSearchResult("Business")
            setbackground(Business_textView)

        }
        Politics_textView.setOnClickListener{
            showSearchResult("Politics")
            setbackground(Politics_textView)

        }
        Tech_textView.setOnClickListener{
            showSearchResult("Tech")
            setbackground(Tech_textView)

        }
        Science_textView.setOnClickListener{
            showSearchResult("Science")
            setbackground(Science_textView)

        }
        Local_news_textView.setOnClickListener{
            showSearchResult("Ahmedabad")
            setbackground(Local_news_textView)
        }
        loadData()

        return view
    }

    private fun loadData() {
        // Show shimmer layout while loading data
        shimmerLayout.visibility = View.VISIBLE
        actualContentLayout.visibility = View.GONE

        // Simulate data loading process (Replace with actual data loading code)
        Handler(Looper.getMainLooper()).postDelayed({
            // Data loading complete
            // Hide shimmer layout and show actual content
            shimmerLayout.visibility = View.GONE
            actualContentLayout.visibility = View.VISIBLE
        }, 1000) // Simulated delay of 3 seconds
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
         handler.postDelayed(runnable, 2000)
    }
    private val runnable=Runnable{
        recycler_view_headLines_horizontalScroll.currentItem=recycler_view_headLines_horizontalScroll.currentItem+1
    }
    private fun setuptransformer(){
        val transformer= CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer{page, position->
            val r=1-abs(position)
            page.scaleY=1f + r * 0.2f
            page.scaleX=1.3f +r *0.2f
        }
        recycler_view_headLines_horizontalScroll.setPageTransformer(transformer)
    }
    fun setbackground(text: TextView) {
        // List of all TextViews
        val textViews = listOf(
            All_news_textView,
            Business_textView,
            Politics_textView,
            Tech_textView,
            Science_textView,
            Local_news_textView
        )

        // Set the text color of the clicked TextView to blue and add blue line
        text.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_blue_dark))
        text.paint.isUnderlineText = true

        // Reset the text color and remove underline for other TextViews
        textViews.forEach { textView ->
            if (textView != text) {
                textView.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
                textView.paint.isUnderlineText = false
            }
        }
    }


    private fun showSearchResult(Search :String){
        recycler_view_headLines_horizontalScroll.visibility=View.GONE
        tv_latest_news.visibility=View.GONE
        searchNewsAdapter.submitList(emptyList())
        shimmerLayoutSmall.visibility = View.VISIBLE

        searchRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchNewsAdapter
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        headlineApi = retrofit.create(headlineapi::class.java)

        searchNews(Search)

    }
    private fun searchNews(query: String) {
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
            }
            shimmerLayoutSmall.visibility = View.GONE
        }
    }
    private fun getHorizontalHeadLineData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
            .create(headlineapi::class.java)

        val retrofitData = retrofitBuilder.getHeadlinesHorizontalScroll()

        retrofitData.enqueue(object : Callback<HeadLineData> {

            override fun onResponse(
                call: Call<HeadLineData>,
                response: Response<HeadLineData>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val articlesArray = responseBody.articles

                        horizontalAdapter = HeadlineHorizontalAdapter(requireContext(), articlesArray, recycler_view_headLines_horizontalScroll)
                        recycler_view_headLines_horizontalScroll.adapter = horizontalAdapter
                        recycler_view_headLines_horizontalScroll.offscreenPageLimit=3
                        recycler_view_headLines_horizontalScroll.clipToPadding=false
                        recycler_view_headLines_horizontalScroll.clipChildren=false
                        recycler_view_headLines_horizontalScroll.getChildAt(0).overScrollMode=RecyclerView.OVER_SCROLL_NEVER
                        //recycler_view_headLines_horizontalScroll.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    } else {
                        // Handle null response body
                        Log.e("HomePage", "Response body is null")
                    }
                } else {
                    // Handle unsuccessful response
                    Log.e("HomePage", "Unsuccessful response: ${response.code()}")
                }
            }



            override fun onFailure(call: Call<HeadLineData>, t: Throwable) {
                Log.d("Vishal", "onFailure :" + t.message)
            }

        })

    }

    private fun getHeadLineData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
            .create(headlineapi::class.java)

        val retrofitData = retrofitBuilder.getHeadlines()

        retrofitData.enqueue(object : Callback<HeadLineData> {

            override fun onResponse(
                call: Call<HeadLineData>,
                response: Response<HeadLineData>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val articlesArray = responseBody.articles

                        myAdapter = HeadLineAdapter(requireContext(), articlesArray, this@FragmentHomePage)
                        recycler_view_headLines.adapter = myAdapter
                    } else {
                        // Handle null response body
                        Log.e("HomePage", "Response body is null")
                    }
                } else {
                    // Handle unsuccessful response
                    Log.e("HomePage", "Unsuccessful response: ${response.code()}")
                }
            }


            override fun onFailure(call: Call<HeadLineData>, t: Throwable) {
                Log.d("Vishal", "onFailure :" + t.message)
            }

        })

    }

    override fun getDetails(title: String, urlToImage: String, content: String, url:String) {
        super.getDetails(title, urlToImage, content, url)
        val intent = Intent(requireContext(), HeadLineDeatilsActivity::class.java)
        intent.putExtra("title", title)
        intent.putExtra("urlToImage", urlToImage)
        intent.putExtra("content", content)
        intent.putExtra("url", url)
        startActivity(intent)
    }

}
