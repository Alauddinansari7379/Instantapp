package com.example.instantapp.cart

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.instantapp.Helper.AppProgressBar
import com.example.instantapp.Helper.myToast
import com.example.instantapp.cart.model.ModelOrderCreate
import com.example.instantapp.databinding.ActivityDeliveryAddressBinding
import com.example.instantapp.login.Login
import com.example.instantapp.login.model.ModelUserSignUp
import com.example.instantapp.mainActivity.MainActivity
import com.example.instantapp.mainActivity.ModelDestoryCart
import com.example.instantapp.retrofit.ApiClient
import com.example.instantapp.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeliveryAddress : AppCompatActivity() {
    private var context=this@DeliveryAddress
    private lateinit var binding:ActivityDeliveryAddressBinding
    lateinit var sessionManager:SessionManager
    var count=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDeliveryAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager=SessionManager(context)

        with(binding){
            imgBack.setOnClickListener {
                onBackPressed()
            }
            btnPlaceOrder.setOnClickListener {
                if (btnPlaceOrder.text.toString() == "Awesome") {
                    startActivity(Intent(this@DeliveryAddress,MainActivity::class.java))
                } else {
                    if (edtAddress.text!!.isEmpty()) {
                        edtAddress.error = "Enter Address"
                        edtAddress.requestFocus()
                        return@setOnClickListener
                    }
                    if (edtCity.text!!.isEmpty()) {
                        edtCity.error = "Enter City Name"
                        edtCity.requestFocus()
                        return@setOnClickListener
                    }

                    if (edtState.text!!.isEmpty()) {
                        edtState.error = "Enter State"
                        edtState.requestFocus()
                        return@setOnClickListener
                    }
                    if (edtZipCode.text!!.isEmpty()) {
                        edtZipCode.error = "Enter ZipCode"
                        edtZipCode.requestFocus()
                        return@setOnClickListener
                    }
                    apiCallMakeOrder()
                }
            }
        }

    }

    private fun apiCallMakeOrder() {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.makeOrder(
            sessionManager.authToken!!,
            "1",
            "1",
            "",
            "",
            "1",
            "2",
            sessionManager.userName,
            sessionManager.userEmail,
            "",
            "",
            binding.edtAddress.text.toString()+binding.edtCity.text.toString(),
            binding.edtZipCode.text.toString(),
            binding.edtState.text.toString(),
            sessionManager.deviceId,
            Checkout.totalAmt,
            "00",
            "00",

        )
            .enqueue(object : Callback<ModelOrderCreate> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<ModelOrderCreate>, response: Response<ModelOrderCreate>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(context, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 404) {
                            myToast(context, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()
                        }  else {
                            myToast(context, response.body()!!.data)
                            binding.layoutAddress.visibility=View.GONE
                            binding.layoutCongratulation.visibility=View.VISIBLE
                            binding.btnPlaceOrder.text="Awesome"
                            binding.appCompatTextView2.text="Order placed"
                            AppProgressBar.hideLoaderDialog()
                            apiCallDestroyCart()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(context, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelOrderCreate>, t: Throwable) {
                    count++
                    if (count <= 2) {
                        apiCallMakeOrder()
                    } else {
                        myToast(context, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()


                }

            })
    }
    private fun apiCallDestroyCart() {
       // AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.destroyCart(
            sessionManager.authToken,
            sessionManager.deviceId.toString()
        )
            .enqueue(object : Callback<ModelDestoryCart> {
                override fun onResponse(
                    call: Call<ModelDestoryCart>, response: Response<ModelDestoryCart>
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
//                            myToast(context, "All Item Deleted")
//                            AppProgressBar.hideLoaderDialog()
//                            val intent = Intent(this@MainActivity, MainActivity::class.java)
//                            intent.flags =
//                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                            finish()
//                            startActivity(intent)
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
                    count++
                    if (count <= 2) {
                        Log.e("count", count.toString())
                        apiCallDestroyCart()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    // AppProgressBar.hideLoaderDialog()


                }

            })
    }


}