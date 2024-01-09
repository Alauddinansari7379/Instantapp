package com.example.instantapp.wishlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.instantapp.Helper.AppProgressBar
import com.example.instantapp.Helper.myToast
import com.example.instantapp.R
import com.example.instantapp.databinding.ActivityWishlistBinding
import com.example.instantapp.home.model.DataX
import com.example.instantapp.home.model.ModelProduct
import com.example.instantapp.listing.adapter.AdapterListing
import com.example.instantapp.retrofit.ApiClient
import com.example.instantapp.sharedpreferences.SessionManager
import com.example.instantapp.wishlist.adapter.AdapterWishlist
import com.example.instantapp.wishlist.model.ModelWishlist
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Wishlist : AppCompatActivity() {
     val binding by lazy {
         ActivityWishlistBinding.inflate(layoutInflater)
     }
    val context=this@Wishlist
    lateinit var sessionManager:SessionManager
    var count=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager= SessionManager(context)
        if (sessionManager.authTokenUser!!.isNotEmpty()){
            apiCallGetWishlist()
        }else{
            myToast(this@Wishlist,"User not logedin")
        }
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

    }
    private fun apiCallGetWishlist() {
        // AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.getWishlists(sessionManager.authToken!!,sessionManager.deviceId)
            .enqueue(object : Callback<ModelWishlist> {
                override fun onResponse(
                    call: Call<ModelWishlist>, response: Response<ModelWishlist>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(context, "Server Error")

                        } else if (response.body()!!.data.items.isEmpty()) {
                            binding!!.recyclerView.adapter =
                                AdapterWishlist(context, response.body()!!.data.items)

                            myToast(context, "No Product Found")
                            binding.shimmerListing.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            binding!!.recyclerView.adapter =
                                AdapterWishlist(context, response.body()!!.data.items)
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

                override fun onFailure(call: Call<ModelWishlist>, t: Throwable) {
                      count++
                    if (count <= 2) {
                        Log.e("count", count.toString())
                        apiCallGetWishlist()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()


                }

            })
    }

}