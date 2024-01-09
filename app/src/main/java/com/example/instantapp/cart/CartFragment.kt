package com.example.instantapp.cart

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.instantapp.Helper.AppProgressBar
import com.example.instantapp.Helper.myToast
import com.example.instantapp.R
import com.example.instantapp.cart.adapter.AdapterCart
import com.example.instantapp.cart.model.ModelAddtoCart
import com.example.instantapp.databinding.FragmentCartBinding
import com.example.instantapp.listing.model.cartModel.ModelCart
import com.example.instantapp.login.Login
import com.example.instantapp.login.SignUp
import com.example.instantapp.mainActivity.ModelDestoryCart
import com.example.instantapp.retrofit.ApiClient
import com.example.instantapp.sharedpreferences.SessionManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartFragment : Fragment(), AdapterCart.Cart {
    private lateinit var binding: FragmentCartBinding
    private lateinit var sessionManager: SessionManager
    var shimmerFrameLayout: ShimmerFrameLayout? = null

    // private var mainData = ArrayList<Items>()
    var count = 0
    var countRe = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCartBinding.bind(view)
        shimmerFrameLayout = view.findViewById(R.id.shimmer)
        shimmerFrameLayout!!.startShimmer()
        sessionManager = SessionManager(requireContext())

        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val parentView: View = layoutInflater.inflate(R.layout.login_dialog, null)
        bottomSheetDialog.setContentView(parentView)
//        if (parentView.parent != null) {
//            (parentView.parent as ViewGroup).removeView(parentView) // <- fix
//        }
        apiCallCartProduct()
        // MainActivity
        with(binding) {
            // priceUndrline.paintFlags = priceUndrline.paintFlags or STRIKE_THRU_TEXT_FLAG
            btnGoToCheckout.setOnClickListener {
                 if (sessionManager.authTokenUser!!.isEmpty()){
                     try {
                         bottomSheetDialog.show()
                     }catch (e:Exception){
                         e.printStackTrace()
                     }
                }else if(CartItem=="0"){
                    myToast(requireActivity(),"Cart Empty")
                }else{
                     startActivity(Intent(activity,Checkout::class.java))

                 }
            }
            val imgClose = parentView.findViewById<ImageView>(R.id.imgBackDil)
            val login = parentView.findViewById<Button>(R.id.btnLoginDil)
            val signUp = parentView.findViewById<Button>(R.id.btnSignUpDil)

            imgClose.setOnClickListener {

                bottomSheetDialog.dismiss()
            }
            login.setOnClickListener {
                startActivity(Intent(activity, Login::class.java))
            }
            signUp.setOnClickListener {
                startActivity(Intent(activity, SignUp::class.java))
            }

            //priceUndrline.paintFlags = priceUndrline.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }

//        val dialog = BottomSheetDialog(requireContext())
//        val bottomSheet = layoutInflater.inflate(R.layout.bottom_sheet, null)
//
//        bottomSheet.buttonSubmit.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        dialog.setContentView(bottomSheet)
//        dialog.show()

    }
//     override fun onDestroy() {
//        super.onDestroy()
//        if (bottomSheetDialog != null && pDialog.isShowing()) {
//            pDialog.dismiss()
//        }
//    }
    private fun apiCallCartProduct() {
        // AppProgressBar.showLoaderDialog(requireContext())
        ApiClient.apiService.getCart(sessionManager.authToken!!, sessionManager.deviceId)
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
                            myToast(requireActivity(), "Server Error")
                            binding.shimmer.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()


                        } else if (response.body()!!.data.items.size == 0) {
                            binding!!.recyclerView.adapter =
                                activity?.let {
                                    AdapterCart(
                                        it,
                                        response.body()!!.data.items,
                                        this@CartFragment
                                    )
                                }
                            myToast(requireActivity(), "No Item Found")
                            binding.shimmer.visibility = View.GONE
                            CartItem="0"
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            binding!!.recyclerView.adapter =
                                activity?.let {
                                    AdapterCart(
                                        it,
                                        response.body()!!.data.items!!,

                                        this@CartFragment
                                    )
                                }
                            CartItem=""
                            // binding.shimmer.visibility = View.GONE
                            for (i in response.body()!!.data.items) {
                                binding.totalPrice.text = "₹" + i.final_total
                                binding.appCompatTextView.text =
                                    response.body()!!.data.items.size.toString() + " items"

                            }
                            binding.shimmer.visibility = View.GONE

                            AppProgressBar.hideLoaderDialog()


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
                        myToast(requireActivity(), "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()


                }

            })
    }

    override fun addToCart(productId: String) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.addToCart(
            sessionManager.authToken,
            productId,
            "1",
            sessionManager.deviceId.toString()
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
                            myToast(requireActivity(), "Server Error")
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 404) {
                            myToast(requireActivity(), "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 200) {
                            myToast(requireActivity(), "item Added in Cart")
                            apiCallCartProduct()
                            AppProgressBar.hideLoaderDialog()
                        } else {
                            AppProgressBar.hideLoaderDialog()

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(requireActivity(), "Something went wrong")
                        AppProgressBar.hideLoaderDialog()


                    }
                }

                override fun onFailure(call: Call<ModelCart>, t: Throwable) {
                    //myToast(requireActivity(), "Something went wrong")
                    count++
                    if (count <= 2) {
                        Log.e("count", count.toString())
                        addToCart(productId)
                    } else {
                        myToast(requireActivity(), t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    // AppProgressBar.hideLoaderDialog()


                }

            })
    }

    override fun removeToCart(id: String) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.removeToCart(
            sessionManager.authToken,
            id,
            sessionManager.deviceId.toString()
        )
            .enqueue(object : Callback<ModelAddtoCart> {
                override fun onResponse(
                    call: Call<ModelAddtoCart>, response: Response<ModelAddtoCart>
                ) {
                    try {
//                        if (response.code() == 200) {
//                            mainData = response.body()!!.data.info.categories
//                            AppProgressBar.hideLoaderDialog()
//
//                        }
                        if (response.code() == 500) {
                            myToast(requireActivity(), "Server Error")
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 404) {
                            myToast(requireActivity(), "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 200) {
                            myToast(requireActivity(), "item Removed from Cart")
                            apiCallCartProduct()
                            AppProgressBar.hideLoaderDialog()
                        } else {
                            AppProgressBar.hideLoaderDialog()

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(requireActivity(), "Something went wrong")
                        AppProgressBar.hideLoaderDialog()


                    }
                }

                override fun onFailure(call: Call<ModelAddtoCart>, t: Throwable) {
                    //myToast(requireActivity(), "Something went wrong")
                    countRe++
                    if (countRe <= 2) {
                        Log.e("count", countRe.toString())
                        addToCart(id)
                    } else {
                        myToast(requireActivity(), t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    // AppProgressBar.hideLoaderDialog()


                }

            })

     }
companion object{
    var CartItem=""
}

}