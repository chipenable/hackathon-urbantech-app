package com.example.hackathonapp.data

import android.util.Log
import com.example.hackathonapp.model.Cache
import com.example.hackathonapp.model.Config
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import ru.chipenable.hackathonvideoapp.model.util.RxSchedulers
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.random.Random

/**
 * Created by Pavel.B on 30.11.2019.
 */

interface IChannelsApi {

    fun checkCache(urls: List<String>): Cache
    fun getPlaylist(cacheId: String? = null): String

}

class ChannelsApi(val client: OkHttpClient): IChannelsApi {

    companion object{
        val TAG = ChannelsApi::class.java.name
    }

    override fun checkCache(urls: List<String>): Cache {

        var cache = Cache("")

        for (url in urls){

            val request = Request.Builder()
                .url(url)
                .build()


            var response: Response? = null
            try {
                response = client.newCall(request).execute()
            }
            catch (e: IOException){
                continue
            }

            if (!response.isSuccessful){
                continue
            }

            val data = response.body?.string() ?: continue

            try{
                val json = JSONObject(data)
                if (!json.has("cache_id")) {
                    continue
                }

                val cacheId = json.get("cache_id") as? String
                if (cacheId == null || cacheId.isEmpty()){
                    continue
                }

                cache = Cache(cacheId)
                break
            }
            catch(e: JSONException){

            }
        }

        return cache
    }

    override fun getPlaylist(cacheId: String?): String {

        return ""
    }


}
