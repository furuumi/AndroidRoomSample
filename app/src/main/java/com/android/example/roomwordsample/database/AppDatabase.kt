package com.android.example.roomwordsample.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.android.example.roomwordsample.database.data.Word
import com.android.example.roomwordsample.database.data.WordDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

// Room固有の書き方？テーブル更新などが発生したらversionを更新するのかな？
// entitiesにテーブルを追加する感じです
// abstract classにしておくとroomが自動で実装クラスを作ってくれる
@Database(
    entities = arrayOf(
        Word::class,
        //Word::class,
    ),
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // DAOへのアクセス用。テーブルが増えたらこれも増やしていく
    abstract fun wordDao(): WordDao

    companion object {
        // スレッドにはメモリ上の値をキャッシュする機能があるが@Volatileを付けることで必ずメモリ上のデータを読むことを強要できる
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val DATABASE_NAME = "database"

        // DBへの接続or生成
        fun getInstance(
            context: Context,
            scope: CoroutineScope
        ): AppDatabase {
            // synchronizedを使う事でこの中の処理を行えるのは一つのスレッドのみに制限できる
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    // DB生成などのコールバック
    private class WordDatabaseCallback (
        private val scope: CoroutineScope
    ) : Callback() {

        // DB生成時に呼ばれる
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                // このスコープはSupervisorJobなので失敗しても親に影響を与えない
                scope.launch {
                    initDatabase(database)
                }
            }
        }

        // こんな感じで初回データを登録する
        fun initDatabase(database: AppDatabase) {
            val wordDao = database.wordDao()
            wordDao.deleteAll()

            var word = Word("Hello")
            wordDao.insert(word)
            word = Word("World!")
            wordDao.insert(word)

            word = Word("TODO!")
            wordDao.insert(word)
        }
    }
}