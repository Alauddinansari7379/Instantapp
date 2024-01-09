package com.example.instantapp.mainActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.instantapp.Helper.AppProgressBar
import com.example.instantapp.Helper.myToast
import com.example.instantapp.R
import com.example.instantapp.cart.CartFragment
import com.example.instantapp.databinding.ActivityMainBinding
import com.example.instantapp.home.adapter.AdapterProduct
import com.example.instantapp.home.model.DataX
import com.example.instantapp.home.model.ModelProduct
import com.example.instantapp.listing.DetailPage
import com.example.instantapp.mainActivity.adapter.AutoSuggestAdapter
import com.example.instantapp.retrofit.ApiClient
import com.example.instantapp.sharedpreferences.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


class MainActivity : AppCompatActivity() {
    private var context = this@MainActivity
    private lateinit var binding: ActivityMainBinding
    lateinit var sessionManager: SessionManager
    var count = 0
    var countDes = 0
    private lateinit var bottomNav: BottomNavigationView

    @SuppressLint("VisibleForTests")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(context)
        bottomNav = binding.bottomNavigationView
        var delete = false
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.hostFragment)
        val navController = navHostFragment!!.findNavController()
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.bootom_nav_menu)
        binding.bottomNavigationView.setupWithNavController(navController)

        Log.e("authTokenUser", sessionManager.authTokenUser.toString())
        Log.e("userEmail", sessionManager.userEmail.toString())
        Log.e("userName", sessionManager.userName.toString())

        apiCallProductList()
//        if (sessionManager.isLogin) {
//            startActivity(Intent(context, MainActivity::class.java))
//            finish()
//        }
        if (sessionManager.deviceId!!.isEmpty()) {
            val deviceID = getDeviceId(context)
            sessionManager.deviceId = deviceID
        }
        Log.e("DeviceId", sessionManager.deviceId.toString())


        with(binding) {
            imgClose.setOnClickListener {
                layoutTitle.visibility = View.VISIBLE
                val rightSwipe: Animation =
                    AnimationUtils.loadAnimation(this@MainActivity, R.anim.anim_right)
                layoutTitle.startAnimation(rightSwipe)
                layoutSearch.visibility = View.GONE
                binding.SearchView.text.clear()
                SearchView.hideKeyboard()

            }

            imgSearch.setOnClickListener {
                layoutTitle.visibility = View.GONE
                layoutTitle.visibility = View.GONE
                layoutSearch.visibility = View.VISIBLE
                val leftSwipe: Animation =
                    AnimationUtils.loadAnimation(this@MainActivity, R.anim.anim_left)
                layoutSearch.startAnimation(leftSwipe)
                SearchView.requestFocus()
                SearchView.showKeyboard()


            }

            imgSearchNew.setOnClickListener {
                val intent = Intent(context as Activity, SearchActivity::class.java)
                    .putExtra("name", SearchView.text.toString().trim())
                context.startActivity(intent)
             }

            imDelete.setOnClickListener {
                if (CartFragment.CartItem != "0") {
                    SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure want to Delete?")
                        .setCancelText("No")
                        .setConfirmText("Yes")
                        .showCancelButton(true)
                        .setConfirmClickListener { sDialog ->
                            sDialog.cancel()
                            apiCallDestroyCart()
                        }
                        .setCancelClickListener { sDialog ->
                            sDialog.cancel()
                        }
                        .show()

                }
            }
        }


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.fragment_Home -> {
                    binding.imDelete.visibility = View.GONE
                    binding.SearchView.text.clear()
                    binding.imgSearch.visibility = View.VISIBLE
                    binding.layoutSearch.visibility = View.GONE
                    binding.layoutTitle.visibility = View.VISIBLE
                }
                R.id.fragment_categories -> {
                    binding.imDelete.visibility = View.GONE
                    binding.SearchView.text.clear()
                    binding.imgSearch.visibility = View.VISIBLE
                    binding.layoutSearch.visibility = View.GONE
                    binding.layoutTitle.visibility = View.VISIBLE

                }
                R.id.fragment_account -> {
                    binding.imDelete.visibility = View.GONE
                    binding.SearchView.text.clear()
                    binding.layoutSearch.visibility = View.GONE
                    binding.imgSearch.visibility = View.VISIBLE
                    binding.layoutTitle.visibility = View.VISIBLE

                }

                R.id.fragment_cart -> {
                    binding.imDelete.visibility = View.VISIBLE
                    binding.SearchView.text.clear()
                    binding.imgSearch.visibility = View.GONE
                    binding.layoutSearch.visibility = View.GONE
                    binding.layoutTitle.visibility = View.VISIBLE

//                    binding.imgSearch.background =
//                        ContextCompat.getDrawable(context, R.drawable.delete)
                }
            }
        }

    }

    private fun getDeviceId(context: Context): String? {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    }


    private fun EditText.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun EditText.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }

    private fun apiCallDestroyCart() {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.destroyCart(
            sessionManager.authToken,
            sessionManager.deviceId.toString()
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
                            myToast(context, "All Item Deleted")
                            AppProgressBar.hideLoaderDialog()
                            val intent = Intent(this@MainActivity, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            finish()
                            startActivity(intent)
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
                    countDes++
                    if (countDes <= 2) {
                        Log.e("count", countDes.toString())
                        apiCallDestroyCart()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    // AppProgressBar.hideLoaderDialog()


                }

            })
    }

    private fun apiCallProductList() {
        ApiClient.apiService.product(sessionManager.authToken!!)
            .enqueue(object : Callback<ModelProduct> {
                override fun onResponse(
                    call: Call<ModelProduct>, response: Response<ModelProduct>
                ) {
                    try {

                        if (ProductListSearch != null) {

                            //spinner code start
                            //  val items = arrayOfNulls<String>(testListNew.)
                            // mainData = response.body()!!.data.posts.data
                            for (i in response.body()!!.data.posts.data) {
                                if (i.title.isNotEmpty()) {
                                    ProductListSearch.add(i.title)
//                                         when (i1.name) {
//
//                                         }
                                }
//                                 for (i1 in i.categories) {
//
//
//
//                                 }
                            }
//                             for (i in testListNew.result!!.indices) {
//                                 items[i] = testListNew.result!![i].Test_Name
//                             }
//                        val adapter: ArrayAdapter<String?> =
//                            ArrayAdapter(this@ProfileSetting, android.R.layout.simple_list_item_1, items)
//                        binding.spinnerSpecialist.adapter = adapter
//                        progressDialog!!.dismiss()


                            val autoSuggestAdapter = AutoSuggestAdapter(
                                this@MainActivity,
                                android.R.layout.simple_list_item_1, ProductListSearch.toMutableList()
                            )

                            binding.SearchView.setAdapter(autoSuggestAdapter)
                            binding.SearchView.threshold = 1


                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(context, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelProduct>, t: Throwable) {
                    count++
                    if (count <= 2) {
                        Log.e("count", count.toString())
                        apiCallProductList()
                    } else {
                        myToast(this@MainActivity, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()


                }

            })
    }
    companion object{
        val ProductListSearch = ArrayList<String>()

    }

}


