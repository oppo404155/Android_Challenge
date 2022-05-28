package com.example.androidchallenge.data.remote

import java.net.HttpURLConnection
import java.net.URL

object RemoteConnection {
    fun getRemoteConnection(): HttpURLConnection {
        val url = URL("http://www.intsabug.com")
        return url.openConnection() as HttpURLConnection
    }
}