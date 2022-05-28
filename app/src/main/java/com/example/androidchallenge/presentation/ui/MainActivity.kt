package com.example.androidchallenge.presentation.ui

import android.app.ProgressDialog
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidchallenge.R
import com.example.androidchallenge.databinding.ActivityMainBinding
import com.example.androidchallenge.presentation.adapters.WordsAdapter
import com.example.androidchallenge.presentation.viewmodel.VMFactory
import com.example.androidchallenge.presentation.viewmodel.WordViewModel

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    val viewmodel: WordViewModel by viewModels { VMFactory(application) }
    var sortState = 0 // 0 indicates to Ascending , 1 indicates to Descending
    val wordadapter = WordsAdapter()
    lateinit var progress: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progress = ProgressDialog(this).apply {
            setMessage("Loading.....")
        }
        progress.show()
        viewmodel.getWordsList()
        viewmodel.wordsState.observe(this) {
            if (!it.isLoading) {
                progress.dismiss()
            }
            if (it.errorMessage != null) {
                Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show()
                progress.dismiss()
            }
            wordadapter.setListItems(it.words)
            progress.dismiss()
        }
        binding.recycler.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = wordadapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView? = searchItem?.actionView as SearchView
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView?.queryHint = getString(R.string.search)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewmodel.onEvent(UiEvent.SearchWord(query = newText!!.lowercase()))
                searchView.clearFocus()
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sortAsc) {
            viewmodel.onEvent(UiEvent.SortWords(Sort.SortAsce))
        } else {
            viewmodel.onEvent(UiEvent.SortWords(Sort.SortDesc))
        }


        return super.onOptionsItemSelected(item)
    }
}