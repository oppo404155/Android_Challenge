package com.example.androidchallenge.presentation.ui

sealed class UiEvent{
    data class SortWords(val sort: Sort):UiEvent()
    data class SearchWord(val query:String):UiEvent()
}
sealed class Sort{
    object SortAsce : Sort()
    object SortDesc : Sort()
}
