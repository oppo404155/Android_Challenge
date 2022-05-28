package com.example.androidchallenge.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class WordsDataBase(context:Context):SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(p0)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {

        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Words.db"
        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${wordsContract.WordEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${wordsContract.WordEntry.COLUMN_NAME_WORD} TEXT," +
                    "${wordsContract.WordEntry.COLUMN_NAME_COUNT} INTEGER)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${wordsContract.WordEntry.TABLE_NAME}"
    }
}