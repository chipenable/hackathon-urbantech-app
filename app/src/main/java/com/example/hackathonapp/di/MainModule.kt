package com.example.hackathonapp.di

import com.example.hackathonapp.data.IAccountApi
import com.example.hackathonapp.model.AccountInteractor
import com.example.hackathonapp.model.Config
import com.example.hackathonapp.model.IAccountInteractor
import com.example.hackathonapp.model.SessionStore
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.chipenable.hackathonvideoapp.model.util.RxSchedulers
import java.net.CookieManager
import java.net.CookiePolicy
import javax.inject.Singleton

/**
 * Created by Pavel.B on 30.11.2019.
 */
@Module
class MainModule {

    @Singleton
    @Provides
    fun provideSchedulers()
            = RxSchedulers()

    @Singleton
    @Provides
    fun provideConfig(): Config {

        val baseUrl = "http://46.61.193.144"

        val cacheUrls = listOf(
            "$baseUrl:8080/cache_id",
            "$baseUrl:8081/cache_id"
        )

        return Config(baseUrl, cacheUrls)
    }

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient {
        val client = OkHttpClient.Builder()

        val logger: Interceptor =

        /*val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        client.cookieJar(JavaNetCookieJar(cookieManager))*/

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
    fun provideSessionStore(): SessionStore {
        return SessionStore()
    }

    @Singleton
    @Provides
    fun provideAccountInteractor(sessionStore: SessionStore, accountApi: IAccountApi,
                                 schedulers: RxSchedulers): IAccountInteractor {
        return AccountInteractor(sessionStore, accountApi, schedulers)
    }
}