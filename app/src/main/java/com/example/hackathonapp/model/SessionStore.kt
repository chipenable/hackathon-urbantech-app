package com.example.hackathonapp.model

/**
 * Created by Pavel.B on 30.11.2019.
 */
class SessionStore(var session: String = "", var userId: String = ""){

    val isAuthorised: Boolean
    get() = session.isNotEmpty()

}