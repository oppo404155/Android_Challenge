package com.example.androidchallenge.data.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.example.androidchallenge.domain.models.Word

class WordORM {

    fun insertWordsListToLocal(context: Context, words: List<Word>) {
        val dataBase = WordsDataBase(context)
        val writableDataBase = dataBase.writableDatabase
        words.forEach {
            val values: ContentValues = postToDataBase(it)
            writableDataBase.insert(
                wordsContract.WordEntry.TABLE_NAME,
                "null", values
            )
        }
        writableDataBase.close()
    }

    fun getWordsListFromLocal(context: Context): List<Word> {
        val dataBase = WordsDataBase(context)
        val readableDataBase = dataBase.readableDatabase
        val cursor: Cursor = readableDataBase.rawQuery(
            "SELECT * FROM " + wordsContract.WordEntry.TABLE_NAME,
            null
        )
        val wordList = ArrayList<Word>()

        if (cursor.count > 0) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val word: Word = cursorToWord(cursor)
                wordList.add(word)
                cursor.moveToNext()
            }

        }

        readableDataBase.close()

        return wordList
    }
    fun clearDataBase(context:Context){
        val dataBase = WordsDataBase(context)
        val readableDataBase = dataBase.readableDatabase
        readableDataBase.delete(wordsContract.WordEntry.TABLE_NAME, null,null)
        readableDataBase.close()
    }

    private fun cursorToWord(cursor: Cursor) = Word(
        word = cursor.getString(cursor.getColumnIndexOrThrow(wordsContract.WordEntry.COLUMN_NAME_WORD)),
        count = cursor.getInt(cursor.getColumnIndexOrThrow(wordsContract.WordEntry.COLUMN_NAME_COUNT))
    )

    private fun postToDataBase(word: Word)= ContentValues().apply {
            put(wordsContract.WordEntry.COLUMN_NAME_WORD, word.word)
            put(wordsContract.WordEntry.COLUMN_NAME_COUNT, word.count)
        }




}

