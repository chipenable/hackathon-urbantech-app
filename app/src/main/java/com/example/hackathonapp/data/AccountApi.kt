package com.example.hackathonapp.data

import com.example.hackathonapp.model.Account
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by Pavel.B on 30.11.2019.
 */
interface IAccountApi {

    @FormUrlEncoded
    @POST("/login")
    fun login(@Field("name") login: String, @Field("password") password: String): Call<Account>

    @GET("/account")
    fun account(@Header("session") session: String, @Header("userId") userId: String): Call<Object>

}