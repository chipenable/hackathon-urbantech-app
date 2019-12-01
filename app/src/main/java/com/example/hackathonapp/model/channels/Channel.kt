package com.example.hackathonapp.model.channels

/**
 * Created by Pavel.B on 30.11.2019.
 */
class Channel(val title: String, val thumbnail: Int?, var isFree: Boolean, var withSubscription: Boolean = false)

data class ChannelStatus(var available: Boolean, var msg: String, var query_id: String, var delta: Int?)

sealed class ChannelEvent {

    class ShowChannel(val position: Int): ChannelEvent()
    class SuggestLogin: ChannelEvent()
    class SuggestSubscription(val position: Int): ChannelEvent()

}