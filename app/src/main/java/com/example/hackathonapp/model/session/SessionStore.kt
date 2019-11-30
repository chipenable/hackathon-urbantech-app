package com.example.hackathonapp.model.session

import android.annotation.SuppressLint
import android.content.SharedPreferences

/**
 * Created by Pavel.B on 30.11.2019.
 */
class SessionStore(private val pref: SharedPreferences){

    var session: String
    var userId: String

    companion object {
        val SESSION_KEY = "session"
        val USER_ID_KEY = "user_id"
    }

    init{
        session = pref.getString(SESSION_KEY, "") ?: ""
        userId = pref.getString(USER_ID_KEY, "") ?: ""
    }

    fun clearSession(){
        saveSession("", "")
    }

    @SuppressLint("ApplySharedPref")
    fun saveSession(session: String, userId: String){
        this.session = session
        this.userId = userId

        pref.edit()
            .putString(SESSION_KEY, session)
            .putString(USER_ID_KEY, userId)
            .commit()
    }

}