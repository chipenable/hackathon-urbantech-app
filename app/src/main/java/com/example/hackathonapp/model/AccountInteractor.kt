package com.example.hackathonapp.model

import android.util.Log
import com.example.hackathonapp.data.IAccountApi
import io.reactivex.Observable
import ru.chipenable.hackathonvideoapp.model.util.RxSchedulers
import java.util.concurrent.TimeUnit

/**
 * Created by Pavel.B on 30.11.2019.
 */

sealed class AuthResult {

    class Success: AuthResult()
    class Error(val message: String): AuthResult()
    class Processing: AuthResult()

}

interface IAccountInteractor {

    fun signIn(login: String, password: String): Observable<AuthResult>

    fun checkSession()

}

class AccountInteractor(val sessionStore: SessionStore, val accountApi: IAccountApi,
                        val schedulers: RxSchedulers): IAccountInteractor {

    companion object{
        val TAG = AccountInteractor::class.java.name
    }

    override fun signIn(login: String, password: String): Observable<AuthResult> {

        val loginObs = Observable.fromCallable {
            accountApi.login(login, password).execute()
        }.map { response ->
            if (response.isSuccessful){
                Log.d(TAG, "success: ${response.body()}")
                val account = response.body()!!
                sessionStore.session = account.session
                sessionStore.userId = account.id
                AuthResult.Success()
            }
            else {
                Log.d(TAG,"error: ${response.message()}")
                AuthResult.Error(response.message())
            }
        }
            .compose(schedulers.fromIoToUiSchedulersObs())


        val delayLoginObs: Observable<AuthResult> = Observable.timer(2, TimeUnit.SECONDS)
            .concatMap { loginObs }


        val processingObs: Observable<AuthResult> = Observable.just(AuthResult.Processing())

        return delayLoginObs.startWith(processingObs)
            .compose(schedulers.fromIoToUiSchedulersObs())
    }

    override fun checkSession(){



        val accountObs = Observable.fromCallable {
            accountApi.account(sessionStore.session, sessionStore.userId).execute()
        }.map {response ->
            Log.d(TAG, "response: ${response.headers()}")
            response
        }
            .compose(schedulers.fromIoToUiSchedulersObs())
            .subscribe()

    }
}