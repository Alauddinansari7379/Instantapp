package com.example.instantapp.account

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.example.instantapp.R
import com.example.instantapp.databinding.FragmentAccountBinding
import com.example.instantapp.order.Order
import com.example.instantapp.setting.Settings
import com.example.instantapp.sharedpreferences.SessionManager
import com.example.instantapp.wishlist.HelpCenter
import com.example.instantapp.wishlist.Wishlist
import com.google.android.material.bottomsheet.BottomSheetDialog

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
     lateinit var sessionManager: SessionManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         return inflater.inflate(R.layout.fragment_account, container, false)
    }
    @SuppressLint("MissingInflatedId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentAccountBinding.bind(view)
        sessionManager= SessionManager(requireContext())

        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val parentView: View = layoutInflater.inflate(R.layout.refrer_dialog, null)
        bottomSheetDialog.setContentView(parentView)

        with(binding){
            if (sessionManager.userName!!.isNotEmpty()){
                userName.text=sessionManager.userName
            }
            cardOrders.setOnClickListener {
                startActivity(Intent(requireContext(),Order::class.java))
            }

            cardSetting.setOnClickListener {
                startActivity(Intent(requireContext(),Settings::class.java))
            }

            cardWishlist.setOnClickListener {
                startActivity(Intent(requireContext(),Wishlist::class.java))
            }

            cardHelp.setOnClickListener {
                startActivity(Intent(requireContext(),HelpCenter::class.java))
            }
            cartRefer.setOnClickListener {
                if (sessionManager.authTokenUser!!.isNotEmpty()) {
                    try {
                        bottomSheetDialog.show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                val imgClose = parentView.findViewById<ImageView>(R.id.imgBackDilRefer)
//                val login = parentView.findViewById<Button>(R.id.btnLoginDil)
//                val signUp = parentView.findViewById<Button>(R.id.btnSignUpDil)

                imgClose.setOnClickListener {
                    bottomSheetDialog.dismiss()
                }
            }
        }
    }


}