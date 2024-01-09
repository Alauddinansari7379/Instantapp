package com.example.instantapp.listing

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.instantapp.Helper.AppProgressBar
import com.example.instantapp.Helper.myToast
import com.example.instantapp.Helper.vibrateOnce
import com.example.instantapp.R
import com.example.instantapp.databinding.ActivityDetailPageBinding
import com.example.instantapp.home.adapter.AdapterProduct
import com.example.instantapp.home.model.DataX
import com.example.instantapp.home.model.ModelProduct
import com.example.instantapp.listing.adapter.AdapterListing
import com.example.instantapp.listing.model.Data
import com.example.instantapp.listing.model.ModelProductDetial
import com.example.instantapp.listing.model.cartModel.ModelCart
import com.example.instantapp.login.Login
import com.example.instantapp.mainActivity.ModelDestoryCart
import com.example.instantapp.retrofit.ApiClient
import com.example.instantapp.sharedpreferences.SessionManager
import com.example.instantapp.wishlist.adapter.AdapterWishlist
import com.example.instantapp.wishlist.model.Item
import com.example.instantapp.wishlist.model.ModelWishListId
import com.example.instantapp.wishlist.model.ModelWishlist
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.String
import kotlin.Exception
import kotlin.Throwable
import kotlin.plus
import kotlin.toString
import kotlin.with

class DetailPage : AppCompatActivity() {
    private var context = this@DetailPage
    private lateinit var binding: ActivityDetailPageBinding
    private lateinit var sessionManager: SessionManager
    private var mainData = ArrayList<Data>()
    var productId = ""
    private var itemCount = 1
    private var count = 0
    private var countwishadd = 0
    private var countCart = 0
    private var countWish = 0
    private var countWishRe = 0
    var value = ""
    var wishlistId = ""
    var productTitle = ""


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(context)
        productId = intent.getStringExtra("id").toString()
        value = intent.getStringExtra("value").toString()
        Log.e("value", value)

        Log.e("productId", productId)

        apiCallProductDetail()
        apiCallProduct()
        apiCallGetWishlist()

        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }
            btnAddToCart.setOnClickListener {
                startActivity(Intent(this@DetailPage, Login::class.java))
            }
            colBlack.setOnClickListener {
                tvColourName.text = "Black"
                colBlack.background = ContextCompat.getDrawable(context, R.drawable.ring_bg)
                colRed.background = ContextCompat.getDrawable(context, R.drawable.red_dot)
                colBlue.background = ContextCompat.getDrawable(context, R.drawable.green_dot)
            }
            colRed.setOnClickListener {
                tvColourName.text = "Red"
                colBlack.background = ContextCompat.getDrawable(context, R.drawable.black_dot)
                colRed.background = ContextCompat.getDrawable(context, R.drawable.ring_bg)
                colBlue.background = ContextCompat.getDrawable(context, R.drawable.green_dot)
            }
            colBlue.setOnClickListener {
                tvColourName.text = "Green"
                colBlack.background = ContextCompat.getDrawable(context, R.drawable.black_dot)
                colRed.background = ContextCompat.getDrawable(context, R.drawable.red_dot)
                colBlue.background = ContextCompat.getDrawable(context, R.drawable.ring_bg)
            }

            layoutSmall.setOnClickListener {
                tvSamll.setTextColor(Color.parseColor("#99C354"))
                tvMedium.setTextColor(Color.parseColor("#FF000000"))
                tvLarge.setTextColor(Color.parseColor("#FF000000"))
            }

            layoutMedium.setOnClickListener {
                tvMedium.setTextColor(Color.parseColor("#99C354"))
                tvSamll.setTextColor(Color.parseColor("#FF000000"))
                tvLarge.setTextColor(Color.parseColor("#FF000000"))
            }
            layoutLarge.setOnClickListener {
                tvLarge.setTextColor(Color.parseColor("#99C354"))
                tvSamll.setTextColor(Color.parseColor("#FF000000"))
                tvMedium.setTextColor(Color.parseColor("#FF000000"))
            }
            layoutPlush.setOnClickListener {
                vibrateOnce(context)
                itemCount++
                tvCount.text = String.valueOf(itemCount)
            }

            layoutDelete.setOnClickListener {
                if (itemCount != 1) {
                    if (itemCount != 0) {
                        itemCount--
                        vibrateOnce(context)
                        tvCount.text = String.valueOf(itemCount)
                    }
                }
            }
            btnAddToCart.setOnClickListener {
                apiCallAddToCart()
            }

            wishList.setOnClickListener {
                if (sessionManager.authTokenUser!!.isNotEmpty()) {
                    apiCallAddToWishList()
                } else {
                    myToast(this@DetailPage, "Please Login first")
                }
            }
            wishListRed.setOnClickListener {
                apiCallRemoveToWishList()
            }


        }


    }

    private fun apiCallProductDetail() {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.getProductDetail(sessionManager.authToken, productId)
            .enqueue(object : Callback<ModelProductDetial> {
                override fun onResponse(
                    call: Call<ModelProductDetial>, response: Response<ModelProductDetial>
                ) {
                    try {
//                        if (response.code() == 200) {
//                            mainData = response.body()!!.data.info.categories
//                            AppProgressBar.hideLoaderDialog()
//
//                        }
                        if (response.code() == 500) {
                            myToast(context, "Server Error")
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 404) {
                            myToast(context, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.data.info.categories.isEmpty()) {
                            myToast(context, "No Product Found")
                            //  binding.shimmerListing.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            productTitle = response.body()!!.data.info.title
                            binding.tvTitle.text = response.body()!!.data.info.title
                            binding.tvPrice.text =
                                "₹" + response.body()!!.data.info.price.price.toString()

                            for (i in response.body()!!.data.info.medias) {
                                Log.e("image", "https" + i.name)
                                Picasso.get().load("${sessionManager.baseURL + i.name}")
                                    .networkPolicy(
                                        NetworkPolicy.NO_CACHE
                                    )
                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                    .placeholder(R.drawable.placeholder_n).stableKey("id")
                                    .into(binding.image)
                            }


//                            binding!!.recyclerView.adapter = AdapterListing(context, response.body()!!.data.posts.data)
//                            binding!!.recyclerView.layoutManager =
//                                GridLayoutManager(context, 2)
//
//                            binding.shimmerListing.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(context, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()


                    }
                }

                override fun onFailure(call: Call<ModelProductDetial>, t: Throwable) {
                    //myToast(requireActivity(), "Something went wrong")
                    apiCallProductDetail()
                    AppProgressBar.hideLoaderDialog()


                }

            })
    }

    private fun apiCallAddToCart() {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.addToCart(
            sessionManager.authToken,
            productId,
            itemCount.toString(),
            sessionManager.deviceId
        )
            .enqueue(object : Callback<ModelCart> {
                override fun onResponse(
                    call: Call<ModelCart>, response: Response<ModelCart>
                ) {
                    try {
//                        if (response.code() == 200) {
//                            mainData = response.body()!!.data.info.categories
//                            AppProgressBar.hideLoaderDialog()
//
//                        }
                        if (response.code() == 500) {
                            myToast(context, "Server Error")
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 404) {
                            myToast(context, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 200) {
                            myToast(context, "item Added in Cart")
                            AppProgressBar.hideLoaderDialog()
                        } else {
                            AppProgressBar.hideLoaderDialog()

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(context, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()


                    }
                }

                override fun onFailure(call: Call<ModelCart>, t: Throwable) {
                    //myToast(requireActivity(), "Something went wrong")
                    countCart++
                    if (countCart <= 2) {
                        Log.e("count", countCart.toString())
                        apiCallAddToCart()
                    } else {
                        myToast(this@DetailPage, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    // AppProgressBar.hideLoaderDialog()


                }

            })
    }

    private fun apiCallProduct() {
        // AppProgressBar.showLoaderDialog(requireContext())
        ApiClient.apiService.product(sessionManager.authToken!!)
            .enqueue(object : Callback<ModelProduct> {
                override fun onResponse(
                    call: Call<ModelProduct>, response: Response<ModelProduct>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(context, "Server Error")

                        } else if (response.body()!!.data.posts.data.isEmpty()) {
                            binding!!.recyclerViewMen.adapter =
                                AdapterListing(context, response.body()!!.data.posts.data)

                            myToast(context, "No Product Found")
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

                            mainList = when (value) {
                                "MEN" -> {
                                    men
                                }
                                "WOMEN" -> {
                                    women
                                }
                                "KIDS" -> {
                                    kids
                                }
                                else -> {
                                    response.body()!!.data.posts.data
                                }
                            }

                            binding!!.recyclerViewMen.adapter =
                                AdapterProduct(context, mainList)
//                            binding!!.recyclerViewMen.layoutManager =
//                                GridLayoutManager(context, 2)
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
                    apiCallProduct()
                    count++
                    if (count <= 2) {
                        Log.e("count", count.toString())
                        apiCallProduct()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()


                }

            })
    }

    private fun apiCallAddToWishList() {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.addToWishlist(
            sessionManager.authToken,
            sessionManager.userEmail,
            productId,
            sessionManager.deviceId
        )
            .enqueue(object : Callback<ModelDestoryCart> {
                override fun onResponse(
                    call: Call<ModelDestoryCart>, response: Response<ModelDestoryCart>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(context, "Server Error")
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 404) {
                            myToast(context, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 200) {
//                            binding.wishListRed.visibility = View.VISIBLE
//                            binding.wishList.visibility = View.GONE
                            apiCallGetWishlist()
                            AppProgressBar.hideLoaderDialog()
                        } else {
                            AppProgressBar.hideLoaderDialog()

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(context, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()


                    }
                }

                override fun onFailure(call: Call<ModelDestoryCart>, t: Throwable) {
                    //myToast(requireActivity(), "Something went wrong")
                    countWish++
                    if (countWish <= 2) {
                        Log.e("count", countWish.toString())
                        apiCallAddToWishList()
                    } else {
                        myToast(this@DetailPage, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    // AppProgressBar.hideLoaderDialog()


                }

            })
    }

    private fun apiCallRemoveToWishList() {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.removeWishlist(sessionManager.authToken, wishlistId)
            .enqueue(object : Callback<ModelDestoryCart> {
                override fun onResponse(
                    call: Call<ModelDestoryCart>, response: Response<ModelDestoryCart>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(context, "Server Error")
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 404) {
                            myToast(context, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 200) {
//                            binding.wishListRed.visibility = View.GONE
//                            binding.wishList.visibility = View.VISIBLE
                            apiCallGetWishlist()
                            AppProgressBar.hideLoaderDialog()
                        } else {
                            AppProgressBar.hideLoaderDialog()

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(context, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelDestoryCart>, t: Throwable) {
                    //myToast(requireActivity(), "Something went wrong")
                    countWishRe++
                    if (countWishRe <= 2) {
                        Log.e("count", countWishRe.toString())
                        apiCallRemoveToWishList()
                    } else {
                        myToast(this@DetailPage, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    // AppProgressBar.hideLoaderDialog()


                }

            })
    }

    private fun apiCallGetWishlist() {
        // AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.getWishlists(sessionManager.authToken!!, sessionManager.deviceId)
            .enqueue(object : Callback<ModelWishlist> {
                override fun onResponse(
                    call: Call<ModelWishlist>, response: Response<ModelWishlist>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(context, "Server Error")

                        } else if (response.body()!!.data.items.isEmpty()) {
//                            binding!!.recyclerView.adapter = AdapterWishlist(context, response.body()!!.data.items)
//
//                            myToast(context, "No Product Found")
//                            binding.shimmerListing.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            val wishlistData = ArrayList<ModelWishListId>()
                            for (i in response.body()!!.data.items) {
                                wishlistData.add(ModelWishListId(i.id.toString(), i.term_id,i.term_title))
                            }
                            var wishlistTerm = ""
                            for (i in wishlistData) {
                                if (i.termId == productId) {
                                    wishlistTerm = "1"
                                 }
                                // val wishlistTerm= wishlistData.find { it.termId==productId
                            }
                            for (i in wishlistData){
                                if (i.termTitle.contentEquals(productTitle)) {
                                    Log.e("i.termTitle", i.termTitle.toString())
                                    wishlistId = i.id
                                }
                            }

                            // val wishlistId= wishlistData.find { it==productId }


                            Log.e("wishlistId", wishlistId.toString())
                            Log.e("productTitle", productTitle.toString())
                            Log.e("wishlistTerm", wishlistTerm.toString())
                            if (wishlistTerm != "1") {
                                binding.wishListRed.visibility = View.GONE
                                binding.wishList.visibility = View.VISIBLE
                            } else {
                                binding.wishListRed.visibility = View.VISIBLE
                                binding.wishList.visibility = View.GONE

                            }
//                            binding!!.recyclerView.adapter =
//                                AdapterWishlist(context, response.body()!!.data.items)
//                            binding!!.recyclerView.layoutManager =
//                                GridLayoutManager(context, 2)
//
//                            binding.shimmerListing.visibility = View.GONE
//                            AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(context, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    }
                }

                override fun onFailure(call: Call<ModelWishlist>, t: Throwable) {
                    countwishadd++
                    if (countwishadd <= 2) {
                        Log.e("count", countwishadd.toString())
                        apiCallGetWishlist()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()


                }

            })
    }

    private fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

}