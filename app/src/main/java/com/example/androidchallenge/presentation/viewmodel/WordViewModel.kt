package com.example.androidchallenge.presentation.viewmodel

import android.app.Application
import android.os.Looper
import android.util.Log
import androidx.core.os.HandlerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.androidchallenge.data.local.WordORM
import com.example.androidchallenge.data.parser.HTMLParserImp
import com.example.androidchallenge.data.remote.RemoteConnection
import com.example.androidchallenge.data.repositories.PageRepoImp
import com.example.androidchallenge.domain.models.Word
import com.example.androidchallenge.presentation.ui.WordsState
import com.example.androidchallenge.utiles.Resource
import java.util.concurrent.Executors

class WordViewModel(application: Application) : AndroidViewModel(application) {


    // the viewModel  in this  project works as a class injector due to restrictions of using
    // third party library like Hilt for DI .


    private val pageRepo = PageRepoImp(
        HTMLParserImp(), Executors.newFixedThreadPool(4),
        RemoteConnection,
        WordORM(),
        application.applicationContext
    )
    val wordsState = MutableLiveData<WordsState>()
    private val currentWordsList = ArrayList<Word>()
//    private val filterList = currentWordsList
//    fun onEvent(uievent: UiEvent) {
//        when (uievent) {
//            is UiEvent.SearchWord -> {
//
//
////                currentWordsList.filter {
////                    it.word.lowercase().contains(uievent.query.lowercase())
////                }
////
////                wordsState.postValue(WordsState(currentWordsList))
//
//            }
//            is UiEvent.SortWords -> {
//                when (uievent.sort) {
//                    is Sort.SortAsce -> {
//                        val sortedList = currentWordsList
//                        sortedList.sortBy {
//                            it.word
//                        }
//                        wordsState.postValue(WordsState(sortedList))
//
//                    }
//                    is Sort.SortDesc -> {
//                        currentWordsList.sortByDescending {
//                            it.word
//                        }
//                        wordsState.postValue(WordsState(currentWordsList))
//
//                    }
//                }
//            }
//        }
//
//    }

    fun getWordsList() {
        pageRepo.getMapOfWords(HandlerCompat.createAsync(Looper.getMainLooper())) { result ->
            when (result) {
                is Resource.Successes -> {
                    wordsState.postValue(result.data?.let {
                        currentWordsList.addAll(it)
                        Log.d("TESTTest", it.toString())
                        WordsState(words = it.sortedBy { it.word })
                    })

                }
                is Resource.Loading -> {
                    wordsState.postValue(WordsState(isLoading = result.isLoading))
                    Log.d("TESTTest", result.isLoading.toString())

                }

                is Resource.Error -> {
                    wordsState.postValue(WordsState(errorMessage = result.error.toString()))
                    Log.d("TESTTest", result.error.toString())

                }



            }

        }
    }
}