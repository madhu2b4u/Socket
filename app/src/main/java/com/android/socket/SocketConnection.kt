package com.android.socket

import io.socket.client.IO
import io.socket.client.Socket
import okhttp3.OkHttpClient

class SocketConnection {

    fun openSocketConnection(): Socket {
        val okHttpClient = OkHttpClient().newBuilder()
        val httpClient = okHttpClient.build()
        IO.setDefaultOkHttpWebSocketFactory(httpClient)
        IO.setDefaultOkHttpCallFactory(httpClient)
        val options = IO.Options()
        options.callFactory = httpClient
        options.webSocketFactory = httpClient
        options.secure = true
        options.query = "token=" + "123";
        return IO.socket("http://visakhafabricare.in:1989", options)
        }
    }


