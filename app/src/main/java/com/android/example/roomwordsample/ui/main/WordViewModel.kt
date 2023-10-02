package com.android.example.roomwordsample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.android.example.roomwordsample.database.data.Word
import com.android.example.roomwordsample.repository.WordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WordViewModel(private val repository: WordRepository) : ViewModel() {

    // Flowにしているのでvalのデータだが自動的に更新される
    val allWords: LiveData<List<Word>> = repository.allWords.asLiveData()

    /*
    viewModelScope.launch
    →viewModelのScopeでスレッド生成
    Dispatchers.IO
    →ディスクまたはNWのI/Oを実行するのに適したスレッド
    Dispatchers.Main
    →メインスレッド(UI用)
    Dispatchers.Default
    →メインスレッド外部で負荷の高い処理を行うときに使う(JSONやXMLの解析など？)
     */
    fun insert(word: Word) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(word)
    }
}

/*
　ViewModelProvider.Factory を使用することにより、
　ViewModelは構成変更(回転などでアクティビティが終了するなど)があっても存在し続けます
 */
class WordViewModelFactory(private val repository: WordRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}