package com.example.instantapp.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.instantapp.databinding.ActivitySignUpBinding

class SignUp : AppCompatActivity() {
    private lateinit var binding:ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding){
            imgBack.setOnClickListener {
                onBackPressed()
            }
        }

    }
}