package com.example.newsapp2.View
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.newsapp2.R
import com.example.newsapp2.Utils.DatabaseHelper
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var databaseHelper: DatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)
        bottomNavigationView = findViewById(R.id.bottom_nav_bar)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_bar_home -> replaceFragment(FragmentHomePage())
                R.id.nav_bar_search -> replaceFragment(FragmentSearch())
                R.id.nav_bar_save -> replaceFragment(FragmentBookMark())
            }
            true
        }
        if (savedInstanceState == null) {
            replaceFragment(FragmentHomePage())
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_home_page, fragment)
            .commit()
    }
}