package com.example.instantapp.home.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Secure
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.afdhal_fa.imageslider.`interface`.ItemClickListener
import com.afdhal_fa.imageslider.model.SlideUIModel
import com.example.instantapp.Helper.AppProgressBar
import com.example.instantapp.Helper.myToast
import com.example.instantapp.R
import com.example.instantapp.databinding.FragmentHomeBinding
import com.example.instantapp.home.adapter.AdapterProduct
import com.example.instantapp.home.model.DataX
import com.example.instantapp.home.model.ModelProduct
import com.example.instantapp.home.model.modelSlider.ModelSlider
import com.example.instantapp.listing.Listing
import com.example.instantapp.retrofit.ApiClient
import com.example.instantapp.sharedpreferences.SessionManager
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sessionManager: SessionManager
    private var mainData = ArrayList<DataX>()
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    var count = 0
    var countS = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        sessionManager = SessionManager(requireContext())
        shimmerFrameLayout = view.findViewById(R.id.shimmer)
        shimmerFrameLayout!!.startShimmer()
        sessionManager.baseURL = "https://storefront.sellacha.com/"



        apiCallProduct()
        apiCallSlider()



        with(binding) {
            tvViewAllMen.setOnClickListener {
                val intent = Intent(context as Activity, Listing::class.java)
                    .putExtra("value", "MEN")
                (context as Activity).startActivity(intent)
            }
            tvViewAllWomen.setOnClickListener {
                val intent = Intent(context as Activity, Listing::class.java)
                    .putExtra("value", "WOMEN")
                (context as Activity).startActivity(intent)
            }
            tvViewAllKid.setOnClickListener {
                val intent = Intent(context as Activity, Listing::class.java)
                    .putExtra("value", "KIDS")
                (context as Activity).startActivity(intent)
            }
            tvViewAllSpecial.setOnClickListener {
                val intent = Intent(context as Activity, Listing::class.java)
                    .putExtra("value", "SPECIAL")
                (context as Activity).startActivity(intent)
            }
        }


    }

    private fun apiCallSlider() {
        // AppProgressBar.showLoaderDialog(requireContext())

        ApiClient.apiService.getSlider(sessionManager.authToken)
            .enqueue(object : Callback<ModelSlider> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelSlider>, response: Response<ModelSlider>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(requireActivity(), "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.data.posts.isEmpty()) {

                            myToast(requireActivity(), "No Slider Found")
                            //   AppProgressBar.hideLoaderDialog()

                        } else {
                            val imageList = ArrayList<SlideUIModel>()

                            for (i in response.body()!!.data.posts) {
                                //uploads/506/23/10/1697450898.png
                                imageList.add(
                                    SlideUIModel(sessionManager.baseURL + i.name, "")
                                )
//                                imageList.add(
//                                    SlideUIModel(baseURL + i.name[1], "")
//                                )
//                                imageList.add(
//                                    SlideUIModel(baseURL + i.name[2], "")
//                                )
//                                imageList.add(
//                                    SlideUIModel(baseURL + i.name[3], "")
//                                )


                            }

                            binding.imageSlide.setImageList(imageList)



                            binding.imageSlide.setItemClickListener(object : ItemClickListener {
                                override fun onItemClick(model: SlideUIModel, position: Int) {

//                when (position) {
//                    0 -> {
//                        sessionManager.bookingType = "1"
//                        startActivity(Intent(requireContext(), Specialities::class.java))
//                    }
//                    1 -> {
//                        sessionManager.bookingType = "2"
//                        startActivity(Intent(requireContext(), Specialities::class.java))
//                    }
//                    else -> {
//                         startActivity(Intent(requireContext(), Specialities::class.java))
//                    }
//                }
                                    //  myToast(requireActivity(), "${model.title}")
                                    // Toast.makeText(requireContext(), "${model.title}", Toast.LENGTH_SHORT).show()
                                }
                            })
                            binding.imageSlide.startSliding(2000) // with new period
                            // binding.imageSlide.stopSliding()
                            AppProgressBar.hideLoaderDialog()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelSlider>, t: Throwable) {
                    // myToast(requireActivity(),t.message.toString())
                    // myToast(requireActivity(), "Something went wrong")

                    countS++
                    if (countS <= 2) {
                        Log.e("count", countS.toString())
                        apiCallSlider()
                    } else {
                        myToast(requireActivity(), t.message.toString())
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
                            myToast(requireActivity(), "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 404) {
                            myToast(requireActivity(), "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.data.posts.data.isEmpty()) {
                            binding!!.recyclerViewMen.adapter =
                                activity?.let {
                                    AdapterProduct(
                                        it,
                                        response.body()!!.data.posts.data,
                                    )
                                }
                            myToast(requireActivity(), "No Product Found")
                            binding.shimmer.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            val men = ArrayList<DataX>()
                            val women = ArrayList<DataX>()
                            val kids = ArrayList<DataX>()
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
                            binding!!.recyclerViewMen.adapter =
                                activity?.let {
                                    AdapterProduct(
                                        it, men,
                                    )
                                }
                            binding.shimmer.visibility = View.GONE

                            binding!!.recyclerViewWomen.adapter =
                                activity?.let {
                                    AdapterProduct(
                                        it, women,
                                    )
                                }
                            binding.shimmerWomen.visibility = View.GONE

                            binding!!.recyclerViewKid.adapter =
                                activity?.let {
                                    AdapterProduct(
                                        it, kids,
                                    )
                                }
                            binding.shimmerKid.visibility = View.GONE

                            binding!!.recyclerViewSpecial.adapter =
                                activity?.let {
                                    AdapterProduct(
                                        it, response.body()!!.data.posts.data,
                                    )
                                }
                            binding.shimmerSpecial.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelProduct>, t: Throwable) {
                    //myToast(requireActivity(), "Something went wrong")
                    count++
                    if (count <= 3) {
                        Log.e("count", count.toString())
                        apiCallProduct()
                    } else {
                        myToast(requireActivity(), t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()


                }

            })
    }

/*
    fun filterSelector(position: Int) {
        filterSelector = position
        when (position) {
            0 -> {
                apiCallProduct()
            }
            1 -> {
                orderAdapter!!.notifySelection(orderList, "pending")
            }
            2 -> {
                orderAdapter!!.notifySelection(orderList, "processing")
            }
            3 -> {
                orderAdapter!!.notifySelection(orderList, "pickup")
            }
            4 -> {
                orderAdapter!!.notifySelection(orderList, "completed")
            }
            5 -> {
                orderAdapter!!.notifySelection(orderList, "canceled")
            }
            6 -> {
                orderAdapter!!.notifySelection(orderList, "archived")
            }
        }
    }
*/

/*
    private fun getFilterList(orderResponse: ArrayList<DataX>) {
        myListData = arrayOf(
            filterItemsDM("All", orderResponse.size),
            filterItemsDM("Awaiting Processing", orderResponse.size),
          //  filterItemsDM("Processing", orderResponse.processing),
//            filterItemsDM("Ready For Pickup", orderResponse.pickup),
//            filterItemsDM("Completed", orderResponse.completed),
//            filterItemsDM("Cancelled", orderResponse.canceled),
//            filterItemsDM("Archived", orderResponse.archived)
        )

        binding!!.recyclerViewMen.visibility = View.VISIBLE
        binding!!.recyclerViewMen.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        filterNameAdapter = OrderFilterSelector(context, myListData, 0, this)
        binding!!.recyclerViewMen.adapter = filterNameAdapter
    }
*/
}