package com.example.instantapp.cart

  import android.annotation.SuppressLint
 import android.content.Intent
 import android.os.Bundle
 import android.view.View
 import androidx.appcompat.app.AppCompatActivity
 import com.example.instantapp.Helper.AppProgressBar
 import com.example.instantapp.Helper.myToast
 import com.example.instantapp.Helper.vibrateOnce
 import com.example.instantapp.R
 import com.example.instantapp.cart.model.ModelAddtoCart
 import com.example.instantapp.databinding.ActivityCheckoutBinding
 import com.example.instantapp.retrofit.ApiClient
 import com.example.instantapp.sharedpreferences.SessionManager
 import com.google.android.material.bottomsheet.BottomSheetDialog
 import ng.max.slideview.SlideView
 import retrofit2.Call
 import retrofit2.Callback
 import retrofit2.Response


class Checkout : AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutBinding
    private var context = this@Checkout
    lateinit var sessionManager: SessionManager
    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this@Checkout)
        apiCallCartProduct()
        with(binding) {

            card1Address.setOnClickListener {
                startActivity(Intent(this@Checkout, DeliveryAddress::class.java))
            }

            cardPayment.setOnClickListener {
                startActivity(Intent(this@Checkout, AddCard::class.java))
            }
            imgBack.setOnClickListener {
                onBackPressed()
            }
            val slideView = findViewById<View>(com.example.instantapp.R.id.slideView) as SlideView
            slideView.setOnSlideCompleteListener { // vibrate the device
               vibrateOnce(context)
                // go to a new activity
                startActivity(Intent(context, DeliveryAddress::class.java))
            }


        }


//        swipeButtonNoState.setOnActiveListener(object : OnActiveListener() {
//            fun onActive() {
//                Toast.makeText(this@MainActivity, "Active!", Toast.LENGTH_SHORT).show()
//            }
//        })

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
                            myToast(context, "Server Error")

                        } else if (response.body()!!.data.items.equals("")) {

                            myToast(context, "No Item Found")
                            //  binding.shimmer.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            var fAmt=0
                            for (i in response.body()!!.data.items) {
                                binding.subTotal.text = "₹" + i.final_total
                                fAmt=i.final_total.toInt()
                                 // binding.qty.text= response.body()!!.data.items.size.toString()
                            }
                            binding.deliveryFee.text="₹200"
                            binding.totalAmt.text = "₹"+(fAmt+200).toString()
                            totalAmt = (fAmt+200).toString()

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelAddtoCart>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCallCartProduct()
                    } else {
                        myToast(context, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()


                }

            })
    }
    companion object{
        var totalAmt=""
    }
}