package com.example.androidchallenge.data.parser

import java.io.InputStream
import java.util.regex.Matcher
import java.util.regex.Pattern

class HTMLParserImp : HTMLParser {

    override fun parse(inputStream: InputStream): String {
        val flatString = inputStream.bufferedReader().readText()
        val pattern = Pattern.compile(
            "<([A-Za-z][A-Za-z0-9]*)\\\\b[^>]*>(.*?)</\\\\1>",
            Pattern.DOTALL
        )
        val matcher: Matcher = with(pattern) {
            matcher(flatString)
        }
        return matcher.group(1)

    }
}



