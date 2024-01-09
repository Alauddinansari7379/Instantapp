package com.example.instantapp.cart.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instantapp.Helper.vibrateOnce
import com.example.instantapp.R
import com.example.instantapp.databinding.SingleRowCartBinding
import com.example.instantapp.listing.DetailPage
import com.example.instantapp.sharedpreferences.SessionManager
import com.squareup.picasso.Picasso

class AdapterCart(
    val context: Context,
    var list: ArrayList<com.example.instantapp.cart.model.Items>,
    val cart:Cart
) : RecyclerView.Adapter<AdapterCart.ViewHolder>() {
    lateinit var sessionManager: SessionManager

    inner class ViewHolder(val binding: SingleRowCartBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SingleRowCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        sessionManager= SessionManager(context)
        with(holder){
            with(list[position]){
                binding.priceUndrline.paintFlags = binding.priceUndrline.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.price.text = "₹$subtotal"
                binding.title.text = term_title
                binding.qty.text = qty

                if (list[position].preview != null) {
                    Picasso.get().load("https:"+list[position].preview)
                        .placeholder(R.drawable.placeholder_n)
                        .error(R.drawable.error_placeholder)
                        .into(binding.image)

                }
                 binding.layoutDetail.setOnClickListener {
                    val intent = Intent(context as Activity, DetailPage::class.java)
                        .putExtra("id", term_id)
                    context.startActivity(intent)
                }
                binding.layoutPlush.setOnClickListener {
                    vibrateOnce(context)
                    cart.addToCart(term_id)
                }
                binding.layoutDelete.setOnClickListener {
                    vibrateOnce(context)
                    cart.removeToCart(id)
                }

             }
        }
    }
    interface Cart{
        fun addToCart(toString: String)
        fun removeToCart(toString: String)
    }
}
