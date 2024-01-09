package com.example.instantapp.categories

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instantapp.R
import com.example.instantapp.databinding.FragmentCategoriesBinding
import com.example.instantapp.listing.Listing


class CategoriesFragment : Fragment() {
    private lateinit var binding:FragmentCategoriesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentCategoriesBinding.bind(view)

        with(binding){
            cardMen.setOnClickListener {
                val intent = Intent(context as Activity, Listing::class.java)
                    .putExtra("value", "MEN")
                (context as Activity).startActivity(intent)
            }
            cardWomen.setOnClickListener {
                val intent = Intent(context as Activity, Listing::class.java)
                    .putExtra("value", "WOMEN")
                (context as Activity).startActivity(intent)
            }
            cardKids.setOnClickListener {
                val intent = Intent(context as Activity, Listing::class.java)
                    .putExtra("value", "KIDS")
                (context as Activity).startActivity(intent)
            }

        }
    }


}