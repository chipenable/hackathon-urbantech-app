package com.example.hackathonapp.data

import com.example.hackathonapp.model.channels.ChannelStatus
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Pavel.B on 01.12.2019.
 */
interface IQueryApi {

    @GET("/available")
    fun checkChannelStatus(@Query("query_id") queryId: String): Call<ChannelStatus>

}