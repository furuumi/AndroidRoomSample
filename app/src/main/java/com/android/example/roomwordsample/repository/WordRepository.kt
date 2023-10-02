package com.android.example.roomwordsample.repository

import com.android.example.roomwordsample.database.data.Word
import com.android.example.roomwordsample.database.data.WordDao
import kotlinx.coroutines.flow.Flow

class WordRepository(private val wordDao: WordDao) {

    val allWords: Flow<List<Word>> = wordDao.getAlphabetizedWords()

    fun insert(word: Word) {
        wordDao.insert(word)
    }
}