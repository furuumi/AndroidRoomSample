package com.android.example.roomwordsample

import android.app.Application
import com.android.example.roomwordsample.database.AppDatabase
import com.android.example.roomwordsample.repository.WordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class WordsApplication : Application() {
    // コルーチンのスコープを生成する。
    // →データベースへのデータの入力は UIライフサイクルとは関連していないため、viewModelScopeのようなCoroutineScopeを使うべきではない。 関連しているのはアプリのライフサイクルです。
    // SupervisorJobにしておくことでコルーチン内の処理で例外が起きても親には伝搬しない
    // →DBへの初回データ投入で失敗してもアプリは落ちない
    val applicationScope = CoroutineScope(SupervisorJob())

    // DBを取得する。初回は生成まで行う
    val database by lazy { AppDatabase.getInstance(this, applicationScope) }


    val wordRepository by lazy { WordRepository(database.wordDao()) }
}