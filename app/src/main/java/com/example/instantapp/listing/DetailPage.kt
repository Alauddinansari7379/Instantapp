package com.example.instantapp.listing

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.instantapp.databinding.ActivityDetailPageBinding
import com.example.instantapp.login.Login

class DetailPage : AppCompatActivity() {
    private lateinit var binding:ActivityDetailPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            imgBack.setOnClickListener {
                onBackPressed()
            }
            btnAddToCart.setOnClickListener {
                startActivity(Intent(this@DetailPage,Login::class.java))
            }
        }


    }
}