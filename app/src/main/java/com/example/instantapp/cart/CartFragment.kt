package com.example.instantapp.cart

import android.content.Intent
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instantapp.R
import com.example.instantapp.databinding.FragmentCartBinding

class CartFragment : Fragment() {
    private lateinit var binding:FragmentCartBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentCartBinding.bind(view)

        with(binding){

             priceUndrline.paintFlags = priceUndrline.paintFlags or STRIKE_THRU_TEXT_FLAG

            btnAddToCart.setOnClickListener {
                startActivity(Intent(activity,Checkout::class.java))
            }

            //priceUndrline.paintFlags = priceUndrline.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

 }