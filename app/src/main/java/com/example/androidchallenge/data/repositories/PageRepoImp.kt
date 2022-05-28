package com.example.androidchallenge.data.repositories

import android.content.Context
import android.os.Handler
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
    override fun getMapOfWords(resultHandler: Handler)
            : LiveData<Resource<List<Word>>> {

//        val url = URL("http://www.intsabug.com")
//        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection

        val liveData = MutableLiveData<Resource<List<Word>>>()
        executor.execute {
            resultHandler.post { liveData.postValue(Resource.Loading(isLoading = true)) }
            val localWordsList = wordORM.getWordsListFromLocal(context = context)
            val isLocalDBIsEmpty = localWordsList.isEmpty()
            val shouldLoadFromCash = !isLocalDBIsEmpty
            if (shouldLoadFromCash) {
                resultHandler.post {
                    liveData.postValue(Resource.Loading(isLoading = false))
                    liveData.postValue(Resource.Successes(localWordsList))
                }
            } else {
                val remoteString = try {
                    val connection = remote.getRemoteConnection().apply {
                        connect()
                    }
                    val flatString: String = parser.parse(connection.inputStream)
                    getWordsFrequency(flatString)
                } catch (e: Exception) {
                    resultHandler.post {
                        liveData.postValue(Resource.Error(error = e.localizedMessage))
                    }
                    null

                }
                remoteString?.let { wordList ->
                    wordORM.clearDataBase(context)
                    wordORM.insertWordsListToLocal(context, wordList)
                    resultHandler.post {
                        liveData.postValue(Resource.Successes(wordORM.getWordsListFromLocal(context)))
                        liveData.postValue(Resource.Loading(false))
                    }
                }
            }

        }


  return liveData }

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