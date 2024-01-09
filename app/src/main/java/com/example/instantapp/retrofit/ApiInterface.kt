package com.example.instantapp.retrofit


import com.example.instantapp.cart.model.ModelAddtoCart
import com.example.instantapp.cart.model.ModelOrderCreate
import com.example.instantapp.home.model.ModelProduct
import com.example.instantapp.home.model.modelSlider.ModelSlider
import com.example.instantapp.listing.model.ModelProductDetial
import com.example.instantapp.listing.model.cartModel.ModelCart
import com.example.instantapp.login.model.ModelLogin
import com.example.instantapp.login.model.ModelUserSignUp
import com.example.instantapp.mainActivity.ModelDestoryCart
import com.example.instantapp.order.model.ModelGetOrder
import com.example.instantapp.setting.model.ModelSetting
import com.example.instantapp.wishlist.model.ModelWishlist
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {
    @POST("login")
    fun login(
        @Query("email") email: String,
        @Query("password") password: String,
    ): Call<ModelLogin>

    @POST("login-user")
    fun loginUser(
        @Query("email") email: String,
        @Query("password") password: String,
    ): Call<ModelLogin>

    @POST("register-user")
    fun registerUser(
        @Header("Authorization") authHeader: String?,
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("mobile") mobile: String,
    ): Call<ModelUserSignUp>

    @GET("product")
    fun product(
        @Header("Authorization") Authorization: String,
     ): Call<ModelProduct>


    @Headers("content-type: application/json")
    @POST("get_slider")
    fun getSlider(
        @Header("Authorization") authHeader: String?,
    ): Call<ModelSlider>

    @Headers("content-type: application/json")
    @POST("delet_acct")
    fun deleteAccount(
        @Header("Authorization") authHeader: String?,
        @Query("email") email:String?
    ): Call<ModelDestoryCart>


    @Headers("content-type: application/json")
    @POST("get_pro_detail")
    fun getProductDetail(
        @Header("Authorization") authHeader: String?,
        @Query("id") id: String?,
    ): Call<ModelProductDetial>

    @Headers("content-type: application/json")
    @POST("destroy_cart")
    fun destroyCart(
        @Header("Authorization") authHeader: String?,
        @Query("device_id")device_id:String?
     ): Call<ModelDestoryCart>

    @Headers("content-type: application/json")
    @POST("add_to_cart")
    fun addToCart(
        @Header("Authorization") authHeader: String?,
        @Query("term_id") term_id: String?,
        @Query("qty") qty: String?,
        @Query("device_id")device_id:String?
    ): Call<ModelCart>

    @Headers("content-type: application/json")
    @POST("cart_remove")
    fun removeToCart(
        @Header("Authorization") authHeader: String?,
        @Query("id") id: String?,
         @Query("device_id")device_id:String?
    ): Call<ModelAddtoCart>

    @Headers("content-type: application/json")
    @GET("get_cart")
    fun getCart(
        @Header("Authorization") authHeader: String?,
        @Query("device_id")device_id:String?
    ): Call<ModelAddtoCart>

    @Headers("content-type: application/json")
    @POST("uorders")
    fun getOrder(
        @Header("Authorization") authHeader: String?,
        @Query("email")email:String?
    ): Call<ModelGetOrder>

    @Headers("content-type: application/json")
    @POST("showlist")
    fun orders(
        @Header("Authorization") authHeader: String?,
        @Query("email")email:String?,
        @Query("slug")slug:String?
    ): Call<ModelGetOrder>

    @Headers("content-type: application/json")
    @POST("usettings")
    fun userSettings(
        @Header("Authorization") authHeader: String?,
        @Query("email")email:String?
    ): Call<ModelSetting>

    @Headers("content-type: application/json")
    @POST("add_to_wishlist")
    fun addToWishlist(
        @Header("Authorization") authHeader: String?,
        @Query("email")email:String?,
        @Query("term_id")term_id:String?,
        @Query("device_id")device_id:String?,
    ): Call<ModelDestoryCart>

    @Headers("content-type: application/json")
    @POST("remove_wishlist")
    fun removeWishlist(
        @Header("Authorization") authHeader: String?,
        @Query("id")id:String?,
    ): Call<ModelDestoryCart>

    @Headers("content-type: application/json")
    @POST("get_wishlistsa")
    fun getWishlists(
        @Header("Authorization") authHeader: String?,
        @Query("device_id")device_id:String?,
    ): Call<ModelWishlist>

    @Headers("content-type: application/json")
    @POST("usettings_update")
    fun userSettingsUpdate(
        @Header("Authorization") authHeader: String?,
        @Query("email")email:String?,
        @Query("name")name:String?,
        @Query("mobile")mobile:String?,
        @Query("password_current")password_current:String?,
        @Query("password")password:String?,
    ): Call<ModelDestoryCart>

    @Headers("content-type: application/json")
    @GET("product")
    fun searchProduct(
        @Header("Authorization") authHeader: String?,
        @Query("src")src:String?,
        @Query("type")type:String?,
    ): Call<ModelProduct>

    @POST("make_order")
    fun makeOrder(
        @Header("Authorization") authHeader: String?,
        @Query("customer_type")customer_type:String?,
        @Query("delivery_type")delivery_type:String?,
        @Query("shipping_method")shipping_method:String?,
        @Query("payment_id")payment_id:String?,
        @Query("payment_method")payment_method:String?,
        @Query("payment_status")payment_status:String?,
        @Query("name")name:String?,
        @Query("email")email:String?,
        @Query("phone")phone:String?,
        @Query("comment")comment:String?,
        @Query("address")address:String?,
        @Query("zip_code")zip_code:String?,
        @Query("location")location:String?,
        @Query("device_id")device_id:String?,
        @Query("total")total:String?,
        @Query("discount")discount:String?,
        @Query("tax")tax:String?,
    ): Call<ModelOrderCreate>


//
//
//    @Multipart
//    @POST("adddoctor")
//    fun adddoctor(
//        @Query("nurse_id") nurse_id: String,
//        @Query("idToken") idToken: String,
//        @Query("group") group: String,
//        @Query("name") name: String,
//        @Query("password") password: String,
//        @Query("email") email: String,
//        @Query("address") address: String,
//        @Query("phone") phone: String,
//        @Query("department") department: String,
//        @Query("reg_no") reg_no: String,
//        @Query("profile") profile: String,
//        @Part img_url: MultipartBody.Part,
//        ): Call<ModelNewAppoint>


}