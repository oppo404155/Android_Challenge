package com.example.androidchallenge.presentation.ui

import com.example.androidchallenge.domain.models.Word

data class WordsState(
    val words: List<Word> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage:String?=null
    )
