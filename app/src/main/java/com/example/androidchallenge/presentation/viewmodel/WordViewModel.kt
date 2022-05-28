package com.example.androidchallenge.presentation.viewmodel

import android.app.Application
import android.os.Looper
import androidx.core.os.HandlerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.androidchallenge.data.local.WordORM
import com.example.androidchallenge.data.parser.HTMLParserImp
import com.example.androidchallenge.data.remote.RemoteConnection
import com.example.androidchallenge.data.repositories.PageRepoImp
import com.example.androidchallenge.domain.models.Word
import com.example.androidchallenge.domain.repositories.PageRepo
import com.example.androidchallenge.presentation.ui.Sort
import com.example.androidchallenge.presentation.ui.UiEvent
import com.example.androidchallenge.presentation.ui.WordsState
import com.example.androidchallenge.utiles.Resource
import java.util.concurrent.Executors

class WordViewModel(application: Application) : AndroidViewModel(application) {


// this line can leak the viewModel but it is mandatory for me due to restrictions that you but
  // in this challenge document (don't use any third party Library) so i could not use dependency injection
    // to inject the context or any other classes so i used AndroidViewModel over ViewModel
    // to provide  context object for SQLite

    private val context = getApplication<Application>().applicationContext
    val pageRepo: PageRepo = PageRepoImp(
        HTMLParserImp(), Executors.newFixedThreadPool(4), RemoteConnection,
        WordORM(), context.applicationContext
    )
    val wordsState = MutableLiveData<WordsState>()
    private val currentWordsList= ArrayList<Word>()

    init {
        getWordsList()
    }

    fun onEvent(uievent: UiEvent, callBack: (WordsState) -> Unit) {
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
                    is Sort.SortDesc -> {}
                }
            }
        }

    }

    private fun getWordsList() {
        pageRepo.getMapOfWords(HandlerCompat.createAsync(Looper.getMainLooper())).value.let { result ->
            when (result) {
                is Resource.Successes -> {
                    wordsState.postValue(result.data?.let {
                        currentWordsList.addAll(it)
                        WordsState(words = it)
                    })

                }
                is Resource.Loading -> {
                    wordsState.postValue(WordsState(isLoading = true))

                }

                is Resource.Error -> {
                    wordsState.postValue(WordsState(errorMessage = "Some errors happened"))
                }


            }

        }
    }
}