package com.example.instantapp.order

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instantapp.Helper.AppProgressBar
import com.example.instantapp.Helper.myToast
import com.example.instantapp.R
import com.example.instantapp.databinding.FragmentActiveBinding
import com.example.instantapp.order.adapter.AdapterOrder
import com.example.instantapp.order.model.Info
import com.example.instantapp.order.model.ModelGetOrder
import com.example.instantapp.retrofit.ApiClient
import com.example.instantapp.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class FragmentActive : Fragment() {
    private lateinit var binding: FragmentActiveBinding
    lateinit var sessionManager: SessionManager
    var count = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_active, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentActiveBinding.bind(view)
        sessionManager = SessionManager(requireContext())
        binding.cardProgress.setOnClickListener {
           // startActivity(Intent(this,Order))
        }
        if (sessionManager.userEmail!!.isNotEmpty()){
            apiCallAllOrder()

        }
    }

    private fun apiCallAllOrder() {
        // AppProgressBar.showLoaderDialog(requireContext())
        ApiClient.apiService.orders(sessionManager.authToken!!, sessionManager.userEmail,"pending")
            .enqueue(object : Callback<ModelGetOrder> {
                override fun onResponse(
                    call: Call<ModelGetOrder>, response: Response<ModelGetOrder>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(requireActivity(), "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 404) {
                            myToast(requireActivity(), "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.data.info.isEmpty()) {
                          //  myToast(requireActivity(), "No Order Found")
                            //   binding.shimmer.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            val completed = ArrayList<Info>()
                            val pending = ArrayList<Info>()
                            val canceled = ArrayList<Info>()
                            // mainData = response.body()!!.data.posts.data
                            for (i in response.body()!!.data.info) {
                                when (i.status) {
                                    "completed" -> completed.add(i)
                                    "pending" -> pending.add(i)
                                    "canceled" -> canceled.add(i)
                                }
                               // i.order_content.value.find {  }
                            }

                            Log.e("completed", completed.toString())
                            Log.e("pending", pending.toString())
                            Log.e("canceled", canceled.toString())
                            binding.itemQty.text=pending.size.toString()+" items"
                            binding.totalPrice.text="₹"+response.body()!!.data.total_amount
                            binding!!.recyclerViewMen.adapter =
                                activity?.let {
                                    AdapterOrder(
                                        it, pending,
                                    )
                                }
//                            binding.shimmer.visibility = View.GONE
//
//                            binding!!.recyclerViewWomen.adapter =
//                                activity?.let {
//                                    AdapterProduct(
//                                        it, women,
//                                    )
//                                }
//                            binding.shimmerWomen.visibility = View.GONE
//
//                            binding!!.recyclerViewKid.adapter =
//                                activity?.let {
//                                    AdapterProduct(
//                                        it, kids,
//                                    )
//                                }
//                            binding.shimmerKid.visibility = View.GONE
//
//                            binding!!.recyclerViewSpecial.adapter =
//                                activity?.let {
//                                    AdapterProduct(
//                                        it, response.body()!!.data.posts.data,
//                                    )
//                                }
//                            binding.shimmerSpecial.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelGetOrder>, t: Throwable) {
                    //myToast(requireActivity(), "Something went wrong")
                    count++
                    if (count <= 2) {
                        Log.e("count", count.toString())
                        apiCallAllOrder()
                    } else {
                        myToast(requireActivity(), t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()


                }

            })
    }


}