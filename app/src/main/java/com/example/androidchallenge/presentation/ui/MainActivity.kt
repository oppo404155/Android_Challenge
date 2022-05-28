package com.example.androidchallenge.presentation.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.androidchallenge.R
import com.example.androidchallenge.presentation.viewmodel.VMFactory
import com.example.androidchallenge.presentation.viewmodel.WordViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewmodel: WordViewModel by viewModels { VMFactory(application) }
        viewmodel.getWordsList()
        viewmodel.wordsState.observe(this) {

        }
        Log.d("TestTest", viewmodel.testViewModelCreation)

    }

}