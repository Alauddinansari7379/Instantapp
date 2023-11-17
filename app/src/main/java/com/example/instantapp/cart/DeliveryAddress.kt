package com.example.instantapp.cart

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.instantapp.databinding.ActivityDeliveryAddressBinding

class DeliveryAddress : AppCompatActivity() {
    private lateinit var binding:ActivityDeliveryAddressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDeliveryAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}