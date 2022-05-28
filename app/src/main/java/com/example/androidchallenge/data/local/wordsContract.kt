package com.example.androidchallenge.data.local

import android.provider.BaseColumns

object wordsContract {
    object WordEntry : BaseColumns {
        const val TABLE_NAME = "wordTable"
        const val COLUMN_NAME_WORD = "word"
        const val COLUMN_NAME_COUNT = "count"
    }
}