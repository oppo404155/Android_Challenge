package com.example.androidchallenge.data.parser

import java.io.InputStream

class HTMLParserImp : HTMLParser {

    override fun parse(inputStream: InputStream): String {
        return inputStream.bufferedReader().readText().apply {
            replace("<([A-Za-z][A-Za-z0-9]*)\\b[^>]*>(.*?)</\\1>", "").split(" ")
        }
    }
}



