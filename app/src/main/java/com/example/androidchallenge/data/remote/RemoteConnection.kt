package com.example.androidchallenge.data.remote

import java.net.HttpURLConnection
import java.net.URL

object RemoteConnection {
    fun getRemoteConnection(): HttpURLConnection {
        val url = URL("https://www.instabug.com")
        return url.openConnection() as HttpURLConnection
    }
}