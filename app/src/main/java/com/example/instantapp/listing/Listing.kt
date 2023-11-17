package com.example.instantapp.listing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.instantapp.databinding.ActivityListingBinding

class Listing : AppCompatActivity() {
    private lateinit var binding:ActivityListingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityListingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            imgBack.setOnClickListener {
                onBackPressed()
            }
            cartJakect.setOnClickListener {
                startActivity(Intent(this@Listing,DetailPage::class.java))
            }

            cart1.setOnClickListener {
                startActivity(Intent(this@Listing,DetailPage::class.java))
            }

            cart2.setOnClickListener {
                startActivity(Intent(this@Listing,DetailPage::class.java))
            }

            cart3.setOnClickListener {
                startActivity(Intent(this@Listing,DetailPage::class.java))
            }
        }

    }
}