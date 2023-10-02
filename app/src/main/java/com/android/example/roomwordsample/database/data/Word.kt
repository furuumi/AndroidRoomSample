package com.android.example.roomwordsample.database.data

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/*
　テーブル定義
　Codelabではファイルを分けていたが定義とDAOは一緒の方が正直見やすいので同一ファイルに記載する
 */
@Entity(tableName = "word_table")
data class Word (
    @PrimaryKey @ColumnInfo(name = "word") val word: String
)

/*
　DAO
　上にも書きましたが同一ファイルの方が楽なので自分はこうしています。
 */
@Dao
interface WordDao {

    // kotlinx-coroutines の Flow を使うとDBが更新された後のFlowの更新に必要なコードがRoomで自動生成されます。
    @Query("SELECT * FROM word_table ORDER BY word ASC")
    fun getAlphabetizedWords(): Flow<List<Word>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(word: Word)

    @Query("DELETE FROM word_table")
    fun deleteAll()
}