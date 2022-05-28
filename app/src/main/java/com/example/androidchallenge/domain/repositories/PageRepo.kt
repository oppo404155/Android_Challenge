package com.example.androidchallenge.domain.repositories

import android.os.Handler
import androidx.lifecycle.LiveData
import com.example.androidchallenge.domain.models.Word
import com.example.androidchallenge.utiles.Resource

interface PageRepo {
    fun getMapOfWords(resultHandler:Handler,callBck:(Resource<List<Word>>)->Unit)
}