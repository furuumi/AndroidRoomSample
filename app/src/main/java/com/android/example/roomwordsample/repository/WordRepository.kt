package com.android.example.roomwordsample.repository

import com.android.example.roomwordsample.database.data.Word
import com.android.example.roomwordsample.database.data.WordDao
import kotlinx.coroutines.flow.Flow

/*
　データ「Word」に対するリポジトリ
　今回はDatabeaseのみだが通信で取得したデータをDBに保持しておいて表示
　という場合もあるのでデータ構造毎にリポジトリに分割する
 */
class WordRepository(private val wordDao: WordDao) {

    val allWords: Flow<List<Word>> = wordDao.getAlphabetizedWords()

    fun insert(word: Word) {
        wordDao.insert(word)
    }
}