package com.example.instantapp.listing

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.instantapp.Helper.AppProgressBar
import com.example.instantapp.Helper.myToast
import com.example.instantapp.R
import com.example.instantapp.cart.model.ModelAddtoCart
import com.example.instantapp.databinding.ActivityListingBinding
import com.example.instantapp.home.model.DataX
import com.example.instantapp.home.model.ModelProduct
import com.example.instantapp.listing.adapter.AdapterListing
import com.example.instantapp.mainActivity.MainActivity
import com.example.instantapp.retrofit.ApiClient
import com.example.instantapp.sharedpreferences.SessionManager
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Listing : AppCompatActivity() {
    private lateinit var binding:ActivityListingBinding
     private lateinit var sessionManager:SessionManager
     private var context=this@Listing
    private var mainData = ArrayList<DataX>()
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    var count=0
    var countNew=0
    var value=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityListingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        shimmerFrameLayout = findViewById(R.id.shimmer_listing)
        shimmerFrameLayout!!.startShimmer()
        sessionManager= SessionManager(context)
        value=intent.getStringExtra("value").toString()

        when(value){
            "MEN"->{
                binding.appCompatTextView2.text="Men"
            }
            "WOMEN"->{
                binding.appCompatTextView2.text="Women"
            }
            "KIDS"->{
                binding.appCompatTextView2.text ="Kids"
            }else->{
            binding.appCompatTextView2.text ="Special Deal"
            }
        }
        apiCallProduct()
        apiCallCartProduct()

        with(binding){
            imgBack.setOnClickListener {
                onBackPressed()
            }
            btnFloating.setOnClickListener {
                startActivity(Intent(this@Listing,MainActivity::class.java))
            }

       }

    }
    private fun apiCallProduct() {
        // AppProgressBar.showLoaderDialog(requireContext())
        ApiClient.apiService.product(sessionManager.authToken!!)
            .enqueue(object : Callback<ModelProduct> {
                override fun onResponse(
                    call: Call<ModelProduct>, response: Response<ModelProduct>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.data.posts.data
                            AppProgressBar.hideLoaderDialog()

                        }
                        if (response.code() == 500) {
                            myToast(context, "Server Error")

                        } else if (response.body()!!.data.posts.data.isEmpty()) {
                            binding!!.recyclerView.adapter =
                                AdapterListing(context, response.body()!!.data.posts.data)

                            myToast(context, "No Product Found")
                            binding.shimmerListing.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            val men = java.util.ArrayList<DataX>()
                            val women = java.util.ArrayList<DataX>()
                            val kids = java.util.ArrayList<DataX>()
                            // mainData = response.body()!!.data.posts.data
                            for (i in response.body()!!.data.posts.data) {
                                for (i1 in i.categories) {
                                    if (i1.name.isNotEmpty()) {
                                        when (i1.name) {
                                            "MEN" -> men.add(i)
                                            "WOMEN" -> women.add(i)
                                            "KIDS" -> kids.add(i)
                                        }
                                    }

                                }
                            }
                            var mainList = java.util.ArrayList<DataX>()

                            mainList = when(value){
                                "MEN"->{
                                    men
                                }
                                "WOMEN"->{
                                    women
                                }
                                "KIDS"->{
                                    kids
                                }else->{
                                    response.body()!!.data.posts.data
                                }
                            }

                            binding!!.recyclerView.adapter =
                                AdapterListing(context, mainList)
                            binding!!.recyclerView.layoutManager =
                                GridLayoutManager(context, 2)

                            binding.shimmerListing.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(context, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    }
                }

                override fun onFailure(call: Call<ModelProduct>, t: Throwable) {
                    //myToast(requireActivity(), "Something went wrong")
                     count++
                    if (count <= 2) {
                        Log.e("count", count.toString())
                        apiCallProduct()
                    } else {
                        myToast(this@Listing, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                     AppProgressBar.hideLoaderDialog()


                }

            })
    }
    private fun apiCallCartProduct() {
        // AppProgressBar.showLoaderDialog(requireContext())
        ApiClient.apiService.getCart(sessionManager.authToken!!,sessionManager.deviceId)
            .enqueue(object : Callback<ModelAddtoCart> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<ModelAddtoCart>, response: Response<ModelAddtoCart>
                ) {
                    try {
                        if (response.code() == 200) {
                            // mainData = response.body()!!.data.items!!
                            AppProgressBar.hideLoaderDialog()

                        }
                        if (response.code() == 500) {
                            myToast(this@Listing, "Server Error")

                        } else if (response.body()!!.data.items.equals("")) {

                            myToast(this@Listing, "No Item Found")
                            //  binding.shimmer.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()

                        } else {

                            for (i in response.body()!!.data.items){
                                binding.btnFloating.text= "₹"+i.final_total
                                binding.qty.text= response.body()!!.data.items.size.toString()

                            }

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelAddtoCart>, t: Throwable) {
                    countNew++
                    if (countNew<=3){
                        apiCallCartProduct()
                    }else{
                        myToast(this@Listing, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()


                }

            })
    }

}