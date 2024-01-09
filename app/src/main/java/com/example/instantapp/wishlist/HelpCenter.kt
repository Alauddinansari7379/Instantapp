package com.example.instantapp.wishlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.instantapp.databinding.ActivityHelpCenterBinding

class HelpCenter : AppCompatActivity() {
    val binding by lazy{
        ActivityHelpCenterBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        with(binding){
            imgBack.setOnClickListener {
                onBackPressed()
            }
        }
    }
}