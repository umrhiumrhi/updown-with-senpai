package com.example.updown_with_senpai

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.edit
import com.example.updown_with_senpai.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initSharedPreferences()
        initViews()
    }

    private fun initViews() {
        val activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val score = result.data?.getIntExtra("score", -1)
                    val sharedPreferences = getSharedPreferences("gameSpf", Context.MODE_PRIVATE)
                    val currentHigh = sharedPreferences.getInt("high_score", 999999)

                    if (currentHigh > score!!) {
                        sharedPreferences.edit {
                            putInt("high_score", score)
                            commit()
                        }
                    }
                    val record = sharedPreferences.getInt("high_score", 999999)
                    showHighScore(record)
                }
            }

        binding.gameStartBtn.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            activityResultLauncher.launch(intent)
        }
    }

    private fun initSharedPreferences() {
        val sharedPreferences = getSharedPreferences("gameSpf", Context.MODE_PRIVATE)
        val record = sharedPreferences.getInt("high_score", 999999)
        showHighScore(record)
    }

    private fun showHighScore(record : Int) {
        binding.highScoreTextView.apply {
            text = when (record) {
                999999 -> "기록 없음"
                else -> "최고 점수 : $record"
            }
        }
    }
}