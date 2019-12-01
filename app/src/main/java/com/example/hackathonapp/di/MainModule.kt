package com.example.hackathonapp.di

import android.app.Activity
import android.content.Context
import com.example.hackathonapp.data.ChannelsApi
import com.example.hackathonapp.data.IAccountApi
import com.example.hackathonapp.data.IChannelsApi
import com.example.hackathonapp.data.IQueryApi
import com.example.hackathonapp.model.*
import com.example.hackathonapp.model.account.AccountInteractor
import com.example.hackathonapp.model.account.IAccountInteractor
import com.example.hackathonapp.model.channels.ChannelsInteractor
import com.example.hackathonapp.model.channels.IChannelsInteractor
import com.example.hackathonapp.model.session.SessionInterceptor
import com.example.hackathonapp.model.session.SessionStore
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.chipenable.hackathonvideoapp.model.util.RxSchedulers
import javax.inject.Singleton

/**
 * Created by Pavel.B on 30.11.2019.
 */
@Module
class MainModule(val context: Context) {

    @Singleton
    @Provides
    fun provideContext(): Context{
        return context
    }


    @Singleton
    @Provides
    fun provideSchedulers()
            = RxSchedulers()

    @Singleton
    @Provides
    fun provideConfig(): Config {

        val baseUrl = "http://46.61.193.144"

        val cacheUrls = listOf(
            "$baseUrl:8080/cache_id"
            //"$baseUrl:8081/cache_id"
        )
        val playlistUrl = "$baseUrl/playlist/index.m3u8"

        return Config(baseUrl, cacheUrls, playlistUrl)
    }

    @Singleton
    @Provides
    fun provideClient(sessionStore: SessionStore): OkHttpClient {
        val client = OkHttpClient.Builder()

        val authInterceptor =
            SessionInterceptor(
                sessionStore
            )
        client.addInterceptor(authInterceptor)

        /*val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        client.addInterceptor(logger)*/

        return client.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(config: Config, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(config.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideAccountApi(retrofit: Retrofit): IAccountApi {
        return retrofit.create(IAccountApi::class.java)
    }

    @Singleton
    @Provides
    fun provideQueryApi(retrofit: Retrofit): IQueryApi {
        return retrofit.create(IQueryApi::class.java)
    }

    @Singleton
    @Provides
    fun provideChannelsApi(client: OkHttpClient): IChannelsApi {
        return ChannelsApi(client)
    }

    @Singleton
    @Provides
    fun provideSessionStore(context: Context): SessionStore {
        val pref = context.getSharedPreferences("session", Activity.MODE_PRIVATE)
        return SessionStore(pref)
    }

    @Singleton
    @Provides
    fun provideAccountInteractor(sessionStore: SessionStore, accountApi: IAccountApi,
                                 schedulers: RxSchedulers): IAccountInteractor {
        return AccountInteractor(sessionStore, accountApi, schedulers)
    }

    @Singleton
    @Provides
    fun provideChannelsInteractor(config: Config, channelsApi: IChannelsApi,
                                  queryApi: IQueryApi, schedulers: RxSchedulers): IChannelsInteractor {
        return ChannelsInteractor(config, channelsApi, queryApi, schedulers)
    }
}