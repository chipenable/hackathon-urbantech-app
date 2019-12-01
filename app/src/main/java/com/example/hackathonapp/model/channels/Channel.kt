package com.example.hackathonapp.model.channels

/**
 * Created by Pavel.B on 30.11.2019.
 */
class Channel(val title: String, val thumbnail: Int?, var isFree: Boolean)

class ChannelStatus(var available: Boolean, var message: String, var queryId: String, var delta: Int)


sealed class ChannelEvent {

    class ShowChannel: ChannelEvent()
    class SuggestLogin: ChannelEvent()

}