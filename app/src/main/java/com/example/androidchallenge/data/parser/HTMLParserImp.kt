package com.example.androidchallenge.data.parser

import java.io.InputStream

class HTMLParserImp : HTMLParser {

    override fun parse(inputStream: InputStream): String {
        return inputStream.bufferedReader().readText().replace("\\<.*?>", "")
    }
}


//           val reader = BufferedReader(InputStreamReader(inputStream))
//           with(inputStream) {
//               while ((reader.readLine().also { line = it }) != null) {
//                   stringBuilder.append(line);
//               }
//
//               close()
//           }
//
//
//
//
//        //regular expression to filter all tags to extract only texts
//        return stringBuilder.toString().replace("\\<.*?>", "")

