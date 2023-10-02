package com.android.example.roomwordsample.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.example.roomwordsample.R
import com.android.example.roomwordsample.WordsApplication
import com.android.example.roomwordsample.database.data.Word
import com.android.example.roomwordsample.ui.newword.NewWordActivity
import com.android.example.roomwordsample.ui.newword.NewWordActivity.Companion.EXTRA_REPLY
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity: AppCompatActivity() {

    // WordRepositoryをアプリから取得してWordViewModelをFactoryから生成する
    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory((application as WordsApplication).wordRepository)
    }

    private val newWordActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = WordListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // observeで監視(監視対象データが変更され、アクティビティがフォアグラウンドにある場合に動作する)
        wordViewModel.allWords.observe(this) { words ->
            words?.let {
                adapter.submitList(it)
            }
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewWordActivity::class.java)
            startForResult.launch(intent)
        }
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
            if (result?.resultCode == Activity.RESULT_OK) {
                result.data?.let { data ->
                    data.getStringExtra(EXTRA_REPLY)?.let {
                        val word = Word(it)
                        wordViewModel.insert(word)
                    }
                }
            } else {
                Toast.makeText(applicationContext, R.string.empty_not_saved, Toast.LENGTH_LONG).show()
            }
        }
}