package com.example.updown_with_senpai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.example.updown_with_senpai.databinding.ActivityGameBinding
import com.example.updown_with_senpai.databinding.ActivityMainBinding
import java.util.*
import kotlin.concurrent.timer

class GameActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityGameBinding.inflate(layoutInflater)
    }

    private var time = 4
    private var timerTask : Timer? = null
    private var answer : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        setAnswer()
        startTimer()
    }

    private fun initViews() {
        binding.answerInputEditText.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                checkAnswer()
            }
            false
        }
    }

    private fun startTimer() {
        timerTask = timer(period = 1000) {
            runOnUiThread {
                binding.chatBoxTextView.text = time.toString()
            }
            time -= 1
            if (time == 0) {
                runOnUiThread { binding.chatBoxTextView.text = "맞춰봐!!" }
                timerTask?.cancel()
            }
        }
    }

    private fun setAnswer() {
        answer = Random().nextInt(10000)
    }

    private fun checkAnswer() {
        if (binding.answerInputEditText.text.toString().length != 4) {
            Toast.makeText(this, "4자리를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val userAnswer= binding.answerInputEditText.text.toString().toInt()
        binding.chatBoxTextView.apply {
            text = when {
                userAnswer == answer -> "정확해"
                userAnswer > answer -> "너무 크다"
                else -> "너무 작다"
            }
        }

    }
}