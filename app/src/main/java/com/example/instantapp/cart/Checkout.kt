package com.example.instantapp.cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.instantapp.databinding.ActivityCheckoutBinding

class Checkout : AppCompatActivity() {
    private lateinit var binding:ActivityCheckoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            card1Address.setOnClickListener {
                startActivity(Intent(this@Checkout,DeliveryAddress::class.java))
            }

            cardPayment.setOnClickListener {
                startActivity(Intent(this@Checkout,AddCard::class.java))
            }
            imgBack.setOnClickListener {
                onBackPressed()
            }
        }

    }
}