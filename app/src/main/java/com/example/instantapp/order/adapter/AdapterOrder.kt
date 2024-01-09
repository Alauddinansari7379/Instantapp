package com.example.instantapp.order.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instantapp.databinding.SingleRowOrderBinding
import com.example.instantapp.order.model.Info
import com.example.instantapp.sharedpreferences.SessionManager

class AdapterOrder(
    val context: Context,
    var list: ArrayList<Info>,
 ) : RecyclerView.Adapter<AdapterOrder.ViewHolder>() {
    lateinit var sessionManager: SessionManager

    inner class ViewHolder(val binding: SingleRowOrderBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SingleRowOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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
                 binding.title.text = order_no

//                if (list[position].preview != null) {
//                    Picasso.get().load("https:"+list[position].preview)
//                        .placeholder(R.drawable.placeholder_n)
//                        .error(R.drawable.error_placeholder)
//                        .into(binding.image)
//
//                }


             }
        }
    }
//    interface Cart{
//        fun addToCart(toString: String)
//    }
}
