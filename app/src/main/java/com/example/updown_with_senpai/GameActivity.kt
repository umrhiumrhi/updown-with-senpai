package com.example.updown_with_senpai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.example.updown_with_senpai.databinding.ActivityGameBinding
import java.util.*
import kotlin.concurrent.timer

class GameActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityGameBinding.inflate(layoutInflater)
    }

    private var time = 4
    private var timerTask : Timer? = null
    private var answer : Int = 0
    private var count = 0
    private var pressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        setAnswer()
        startTimer()
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - pressedTime >= 2000) {
            pressedTime = System.currentTimeMillis()
            Toast.makeText(this, "한 번 더 누르면 메인화면으로 갑니다.", Toast.LENGTH_SHORT).show()
        } else {
            finish()
        }
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
                userAnswer == answer -> {
                    "정확해"
                }
                userAnswer > answer -> "너무 크다"
                else -> "너무 작다"
            }
        }

        if (userAnswer == answer) {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("score", count)
            }
            setResult(RESULT_OK, intent)
            finish()
        } else {
            count += 1
        }
    }
}