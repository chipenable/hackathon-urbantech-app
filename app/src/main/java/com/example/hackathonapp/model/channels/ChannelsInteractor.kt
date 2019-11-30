package com.example.hackathonapp.model.channels

import android.util.Log
import com.example.hackathonapp.data.IChannelsApi
import com.example.hackathonapp.model.Cache
import com.example.hackathonapp.model.Config
import io.reactivex.Observable
import ru.chipenable.hackathonvideoapp.model.util.RxSchedulers
import java.util.concurrent.TimeUnit
import kotlin.random.Random

/**
 * Created by Pavel.B on 30.11.2019.
 */
interface IChannelsInteractor {

    fun getPlaylist(): Observable<String>

}

class ChannelsInteractor(private val config: Config, private val channelsApi: IChannelsApi,
                         private val schedulers: RxSchedulers
): IChannelsInteractor {

    companion object {
        val TAG = ChannelsInteractor::class.java.name
    }

    private var lastCache: Cache? = null

    override fun getPlaylist(): Observable<String> {
        val cacheObs = Observable.fromCallable {
            val cache = channelsApi.checkCache(config.cacheUrls)
            Log.d(TAG, "cache: $cache")
            cache
        }

        return Observable.interval(0, 5, TimeUnit.SECONDS)
            .concatMap { cacheObs }
            .map {
                if (Random.nextBoolean()) {
                    it.id = ""
                }
                it
            }
            .filter {
                val result = if (it != lastCache) {
                    lastCache = it
                    true
                } else {
                    false
                }

                Log.d(TAG, "cache filter: $result")
                result
            }
            .map { cache ->
                if (cache.id.isEmpty()) {
                    config.playlistUrl
                } else {
                    "${config.playlistUrl}?cache_id=${cache.id}"
                }

            }

            .compose(schedulers.fromIoToUiSchedulersObs())
    }
}
