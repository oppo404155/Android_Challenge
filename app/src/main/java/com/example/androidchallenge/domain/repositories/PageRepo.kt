package com.example.androidchallenge.domain.repositories

import android.os.Handler
import androidx.lifecycle.LiveData
import com.example.androidchallenge.data.parser.HTMLParserImp
import com.example.androidchallenge.data.repositories.PageRepoImp
import com.example.androidchallenge.domain.models.Word
import com.example.androidchallenge.utiles.Resource
import java.util.concurrent.Flow

interface PageRepo {
    fun getMapOfWords(resultHandler:Handler):LiveData<Resource<List<Word>>>
}