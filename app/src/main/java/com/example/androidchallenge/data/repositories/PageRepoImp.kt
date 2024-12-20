package com.example.androidchallenge.data.repositories

import android.content.Context
import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidchallenge.data.local.WordORM
import com.example.androidchallenge.data.parser.HTMLParser
import com.example.androidchallenge.data.remote.RemoteConnection
import com.example.androidchallenge.domain.models.Word
import com.example.androidchallenge.domain.repositories.PageRepo
import com.example.androidchallenge.utiles.Resource
import java.util.concurrent.Executor

class PageRepoImp constructor(
    private val parser: HTMLParser,
    private val executor: Executor,
    private val remote: RemoteConnection,
    private val wordORM: WordORM,
    private val context: Context
) : PageRepo {
    override fun getMapOfWords(resultHandler: Handler, callBck: (Resource<List<Word>>) -> Unit) {



        executor.execute {
            resultHandler.post { callBck(Resource.Loading(isLoading = true)) }
            val localWordsList = wordORM.getWordsListFromLocal(context = context)

            val isLocalDBIsEmpty = localWordsList.isEmpty()
            val shouldLoadFromCash = !isLocalDBIsEmpty
            if (shouldLoadFromCash) {
                Log.d("ForTest", "Im in the cash now$localWordsList")

                resultHandler.post {
                    callBck(Resource.Loading(isLoading = false))
                    callBck(Resource.Successes(localWordsList))
                }
            } else {
                val remoteString = try {
                    val connection = remote.getRemoteConnection().apply {
                        connect()
                    }
                    val flatString: String = parser.parse(connection.inputStream)
                    Log.d("ForTest", flatString)
                    getWordsFrequency(flatString)

                } catch (e: Exception) {
                    resultHandler.post {
                        Log.d("ForTest", e.localizedMessage)
                        callBck(Resource.Error(error = e.localizedMessage))
                    }
                    null

                }
                remoteString?.let { wordList ->
                    wordORM.clearDataBase(context)
                    wordORM.insertWordsListToLocal(context, wordList)
                    Log.d("HopeToWork", wordList.toString())
                    resultHandler.post {
                        callBck(Resource.Successes(wordORM.getWordsListFromLocal(context)))
                        callBck(Resource.Loading(false))
                    }
                }
            }

        }


    }

    /**
    complexity is equal to O(n) because
    dictionary search is constant so the complexity = (C+n+n) that could be simplified to n
     */
    private fun getWordsFrequency(flatString: String): List<Word> {
        val wordsList = ArrayList<Word>()
        val dict = HashMap<String, Int>()
        for (word in flatString.split(" ")) {
            var value = 1
            if (dict[word] != null)
                value += dict[word] ?: 0

            dict[word] = value

        }

        for ((k, v) in dict) {
            wordsList.add(Word(word = k, count = v))
        }
        return wordsList
    }


}