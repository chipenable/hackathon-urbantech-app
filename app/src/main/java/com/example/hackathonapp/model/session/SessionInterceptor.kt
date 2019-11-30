package com.example.hackathonapp.model.session

import com.example.hackathonapp.model.session.SessionStore
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Created by Pavel.B on 30.11.2019.
 */
class SessionInterceptor(private val sessionStore: SessionStore):
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = customiseRequest(chain.request())
        return chain.proceed(request)
    }

    private fun customiseRequest(request: Request): Request {

        val requestBuilder =  request.newBuilder()
        requestBuilder.addHeader("Cookie",
            "userId=${sessionStore.userId};session=${sessionStore.session}")

        return requestBuilder.build()
    }

}