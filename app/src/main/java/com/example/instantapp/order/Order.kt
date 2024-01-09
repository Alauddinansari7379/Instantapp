package com.example.instantapp.order

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.example.instantapp.R
import com.example.instantapp.databinding.ActivityOrderBinding
import com.example.instantapp.order.adapter.ViewPagerAdapter
import com.example.instantapp.sharedpreferences.SessionManager
import com.google.android.material.tabs.TabLayout

class Order : AppCompatActivity() {
    private var context = this@Order
    val binding by lazy {
        ActivityOrderBinding.inflate(layoutInflater)
    }
    private lateinit var pager: ViewPager // creating object of ViewPager
    private lateinit var tab: TabLayout  // creating object of TabLayout
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)

        with(binding){
            imgBack.setOnClickListener {
                onBackPressed()
            }
        }

        pager = findViewById(R.id.viewPager)
        tab = findViewById(R.id.tabs)
        val adapter = ViewPagerAdapter(supportFragmentManager)

        val tabs = findViewById<View>(R.id.tabs) as TabLayout
        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
                when (tab.position) {
                    0 -> tabs.setSelectedTabIndicatorColor(Color.parseColor("#99C354"))
                    1 -> tabs.setSelectedTabIndicatorColor(Color.parseColor("#99C354"))
                 }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        adapter.addFragment(FragmentActive(), "Active")
        adapter.addFragment(FragmentPast(), "Past")
        pager.adapter = adapter
        tab.setupWithViewPager(pager)
    }
}