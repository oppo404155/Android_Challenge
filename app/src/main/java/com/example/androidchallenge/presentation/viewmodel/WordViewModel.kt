package com.example.androidchallenge.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.core.os.HandlerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidchallenge.data.local.WordORM
import com.example.androidchallenge.data.parser.HTMLParserImp
import com.example.androidchallenge.data.remote.RemoteConnection
import com.example.androidchallenge.data.repositories.PageRepoImp
import com.example.androidchallenge.domain.models.Word
import com.example.androidchallenge.domain.repositories.PageRepo
import com.example.androidchallenge.presentation.ui.MainActivity
import com.example.androidchallenge.presentation.ui.Sort
import com.example.androidchallenge.presentation.ui.UiEvent
import com.example.androidchallenge.presentation.ui.WordsState
import com.example.androidchallenge.utiles.Resource
import java.util.concurrent.Executors

class WordViewModel(application: Application) : AndroidViewModel(application) {


  // the viewModel  in this  project works as a class injector due to restrictions of using
    // third party library like Hilt for DI .

    val pageRepo = PageRepoImp(
        HTMLParserImp(), Executors.newFixedThreadPool(4),
        RemoteConnection,
        WordORM(),
        application.applicationContext)
    val wordsState = MutableLiveData<WordsState>()
    private val currentWordsList = ArrayList<Word>()
    val testViewModelCreation = "TeSTWDLQWDLQ;MX;QSXMQSX"


    fun onEvent(uievent: UiEvent) {
        when (uievent) {
            is UiEvent.SearchWord -> {

            }
            is UiEvent.SortWords -> {
                when (uievent.sort) {
                    is Sort.SortAsce -> {
                        currentWordsList.sortBy {
                            it.word
                        }
                    }
                    is Sort.SortDesc -> {
                        currentWordsList.sortByDescending {
                            it.word
                        }
                    }
                }
            }
        }

    }

 fun getWordsList() {
       pageRepo.getMapOfWords(HandlerCompat.createAsync(Looper.getMainLooper())){ result ->
            when (result) {
                is Resource.Successes -> {
                    wordsState.postValue(result.data?.let {
                        currentWordsList.addAll(it)
                        Log.d("TESTTest", it.toString())
                        WordsState(words = it)
                    })

                }
                is Resource.Loading -> {
                    wordsState.postValue(WordsState(isLoading = result.isLoading))
                    Log.d("TESTTest", result.isLoading.toString())

                }

                is Resource.Error -> {
                    wordsState.postValue(WordsState(errorMessage = "Some errors happened"))
                    Log.d("TESTTest", "Error")

                }


                else -> {
                    Log.d("TEST", "UnKnown")
                }
            }

        }
    }
}