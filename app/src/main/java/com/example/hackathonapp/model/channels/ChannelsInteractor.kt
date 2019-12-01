package com.example.hackathonapp.model.channels

import android.util.Log
import com.example.hackathonapp.data.IChannelsApi
import com.example.hackathonapp.data.IQueryApi
import com.example.hackathonapp.model.Cache
import com.example.hackathonapp.model.Config
import io.reactivex.Observable
import ru.chipenable.hackathonvideoapp.model.util.RxSchedulers


/**
 * Created by Pavel.B on 30.11.2019.
 */

sealed class PlaylistResult{

    class Success(val playlist: String): PlaylistResult()
    class Processing(val message: String): PlaylistResult()
}

interface IChannelsInteractor {

    fun getPlaylist(channelId: Int): Observable<PlaylistResult>
    fun getChannels(isAuthorised: Boolean): Observable<List<Channel>>
}

class ChannelsInteractor(private val config: Config,
                         private val channelsApi: IChannelsApi,
                         private val queryApi: IQueryApi,
                         private val schedulers: RxSchedulers
): IChannelsInteractor {

    companion object {
        val TAG = ChannelsInteractor::class.java.name
        const val CHECK_CACHE = 60000L
    }

    private var lastCache: Cache? = null
    private var queryId: String = ""

    override fun getChannels(isAuthorised: Boolean): Observable<List<Channel>> {
        return Observable.fromCallable { channelsApi.getChannels(isAuthorised) }
    }

    override fun getPlaylist(channelId: Int): Observable<PlaylistResult>{

        val statusObs = Observable.create<PlaylistResult> {

            var isAvailable = false

            Log.d(TAG ,"try to check channel status")
            while(!it.isDisposed){
                val response = queryApi.checkChannelStatus(queryId).execute()
                val status = response.body()!!
                Log.d(TAG, "check status: $status")

                isAvailable = status.available
                if (isAvailable){
                    break
                }

                queryId = status.query_id
                val pause = status.delta ?: 10

                Log.d(TAG, "we should wait $pause seconds")
                it.onNext(PlaylistResult.Processing(status.msg))
                try {
                    Thread.sleep(pause.toLong() * 1000)
                }
                catch (error: InterruptedException){
                    break
                }


            }

            Log.d(TAG, "channel is available. try to get cache")
            queryId = ""
            lastCache = null

            while(!it.isDisposed) {
                val cache = channelsApi.checkCache(config.cacheUrls)
                Log.d(TAG, "cache: $cache")

                if (cache != lastCache) {
                    lastCache = cache

                    val channelPath = if (channelId % 2 == 0) "bbb" else "flag"
                    val url = "${config.baseUrl}/${channelPath}/index.m3u8"

                    val playlist = if (cache.id.isEmpty()) {
                        url
                    } else {
                        "${url}?cache_id=${cache.id}"
                    }
                    Log.d(TAG, "playlist: $playlist")

                    it.onNext(PlaylistResult.Success(playlist))
                }

                try {
                    Thread.sleep(CHECK_CACHE)
                }
                catch (error: InterruptedException){
                    break
                }
            }

            Log.d(TAG, "complete")
            it.onComplete()
        }

        return statusObs.compose(schedulers.fromIoToUiSchedulersObs())
    }
}
