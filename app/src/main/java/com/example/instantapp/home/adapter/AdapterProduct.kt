package com.example.instantapp.home.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.instantapp.R
import com.example.instantapp.home.model.DataX
import com.example.instantapp.listing.DetailPage
import com.squareup.picasso.Picasso


class AdapterProduct(val context: Context,private val list: ArrayList<DataX>) :
    RecyclerView.Adapter<AdapterProduct.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_product, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
             holder.tvTitle.text = list[position].title
            holder.tvPricePro.text = "₹" + list[position].price.price.toString()
            // holder.tvRatingPro.text = list[position].title
            if (list[position].preview != null) {
                Picasso.get().load("https:" + list[position].preview.media.url)
                    .placeholder(R.drawable.placeholder_n)
                    .error(R.drawable.error_placeholder)
                    .into(holder.imgPro)

        }
        holder.itemView.setOnClickListener {
            var category=""
            for (i in list[position].categories){
                category=i.name
            }
            val intent = Intent(context as Activity, DetailPage::class.java)
                .putExtra("id", list[position].id.toString())
                .putExtra("value", category)
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return list.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPro: ImageView = itemView.findViewById(R.id.imgPro)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvPricePro: TextView = itemView.findViewById(R.id.tvPricePro)
        val tvRatingPro: TextView = itemView.findViewById(R.id.tvRatingPro)
        val tvDescPro: TextView = itemView.findViewById(R.id.tvDescPro)


    }
}