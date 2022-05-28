package com.example.androidchallenge.data.parser

import java.io.InputStream

interface HTMLParser {
    fun parse(inputStream: InputStream):String
}