package com.example.instantapp.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.instantapp.databinding.ActivityLoginBinding
import com.example.instantapp.mainActivity.MainActivity

class Login : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding){
            tvForgot.setOnClickListener {
                startActivity(Intent(this@Login,SignUp::class.java))
            }

            btnLogin.setOnClickListener {
                startActivity(Intent(this@Login,MainActivity::class.java))
            }
        }

    }
}