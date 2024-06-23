package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizapp.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var congrats: TextView
    private lateinit var message: TextView
    private lateinit var userName: TextView

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        congrats = binding.tvCongratulations
        userName = binding.tvName
        message = binding.tvResult

        val correctAnswers = intent.getStringExtra(SetData.score)
        val name = intent.getStringExtra(SetData.name)

        if (correctAnswers != null) {
            when (correctAnswers.toInt()) {
                in 10..Int.MAX_VALUE -> {
                    congrats.text = "Congratulations Mr/Ms. "
                    userName.text = name
                    message.text = "Your IQ on genius leve :)"
                }
                in 7..9 -> {
                    congrats.text = "Congratulations Mr/Ms. "
                    userName.text = name
                    message.text = "your IQ is Above Average"
                }
                in 5..6 -> {
                    congrats.text = "Congratulations Mr/Ms. "
                    userName.text = name
                    message.text = "Your IQ is Average"
                }
                else -> {
                    congrats.text = "Sorry Mr/Ms. "
                    userName.text = name
                    message.text = "Your IQ is Below average"
                }
            }
        }


        binding.btnFinish.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
