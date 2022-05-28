package com.example.androidchallenge.presentation.adapters

import android.location.Criteria
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.androidchallenge.databinding.RecyclerItemBinding
import com.example.androidchallenge.domain.models.Word
import com.example.androidchallenge.presentation.ui.Sort
import com.example.androidchallenge.presentation.ui.UiEvent

class WordsAdapter : RecyclerView.Adapter<WordsAdapter.WordViewHolder>(), Filterable {
    private val wordslist = ArrayList<Word>()
    private lateinit var wordslistFull: List<Word>

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
        val word = wordslist[position]
        holder.bind(word)
    }

    override fun getItemCount(): Int {
        return wordslist.size
    }

    fun setListItems(words: List<Word>) {

        wordslist.addAll(words)
        //copy of wordList for filtering
        wordslistFull = ArrayList<Word>(wordslist)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return wordFilter
    }
    private val wordFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<Word> = ArrayList()
            if (constraint.isEmpty()) {
                filteredList.addAll(wordslistFull)
            } else {
                val filterPattern = constraint.toString().lowercase().trim()
                for (item in wordslistFull) {
                    if (item.word.lowercase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            wordslist.clear()
            wordslist.addAll(results.values as List<Word>)
            notifyDataSetChanged()
        }
    }
   fun sort(sortCriteria:Sort){
       when(sortCriteria){
           is Sort.SortAsce->{
               wordslist.sortBy {
                   it.word
               }
           }
           is Sort.SortDesc->{
               wordslist.sortByDescending {
                   it.word

               }
           }
       }
       notifyDataSetChanged()

   }

}