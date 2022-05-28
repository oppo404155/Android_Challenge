package com.example.androidchallenge.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class VMFactory(application: Application) :ViewModelProvider.Factory{
   private val myApp = application

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WordViewModel(myApp) as T
    }

}