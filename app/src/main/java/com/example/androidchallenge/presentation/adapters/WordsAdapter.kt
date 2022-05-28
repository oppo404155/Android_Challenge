package com.example.androidchallenge.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidchallenge.databinding.RecyclerItemBinding
import com.example.androidchallenge.domain.models.Word
import com.example.androidchallenge.presentation.viewmodel.WordViewModel

class WordsAdapter : RecyclerView.Adapter<WordsAdapter.WordViewHolder>() {
    private val wordslist = ArrayList<Word>()

    inner class WordViewHolder(private val binding: RecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(word: Word) {
            binding.apply {
                countTxv.text = word.count.toString()
                wordTxv.text = word.word
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word=wordslist.get(position)
        holder.bind(word)
    }

    override fun getItemCount(): Int {
      return wordslist.size
    }

    fun setListItems(words: List<Word>) {
       wordslist.clear()
        wordslist.addAll(words)
        notifyDataSetChanged()
    }


}