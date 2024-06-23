package com.example.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.quizapp.databinding.ActivityQuestionBinding

class QuestionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionBinding
    private var name: String? = null
    private var score: Int = 0
    private var questionList: ArrayList<SetQuestion>? = null
    private var currentPosition = 1
    private var selectedOption:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        name = intent.getStringExtra(SetData.name)
        questionList = SetData.questionData()
        setQuestion()

        binding.opt1.setOnClickListener {
            selectOption(binding.opt1, 1)
        }

        binding.opt2.setOnClickListener {
            selectOption(binding.opt2, 2)
        }

        binding.opt3.setOnClickListener {
            selectOption(binding.opt3, 3)
        }

        binding.opt4.setOnClickListener {
            selectOption(binding.opt4, 4)
        }

        binding.submit.setOnClickListener {
            if (selectedOption != 0) {
                val question = questionList!![currentPosition - 1]
                if (selectedOption != question.ans) {
                    setColor(selectedOption, R.drawable.wrong_question_option)
                } else {
                    score++
                }
                setColor(question.ans, R.drawable.correct_question_option)
                if (currentPosition == questionList!!.size) {
                    binding.submit.text = "FINISH"
                } else {
                    binding.submit.text = "Go to Next"
                }
            } else {
                currentPosition++
                when {
                    currentPosition <= questionList!!.size->{
                        setQuestion()
                        binding.submit.text="Submit"
                    }
                    else -> {
                        val intent = Intent(this, ResultActivity::class.java)
                        intent.putExtra(SetData.name, name.toString())
                        intent.putExtra(SetData.score, score.toString())
                        intent.putExtra("total size", questionList!!.size.toString())

                        startActivity(intent)
                        finish()
                    }
                }
            }
            selectedOption = 0
        }
    }

    private fun setQuestion() {
        val question = questionList!![currentPosition - 1]
        questionStyle()
        binding.questionText.text = question.question
        binding.opt1.text = question.one
        binding.image.setImageResource(question.img)
        binding.opt2.text = question.tw0
        binding.opt3.text = question.three
        binding.opt4.text = question.four
        binding.progressBar.progress = currentPosition
        binding.progressBar.max = questionList!!.size
        binding.progressText.text = "${currentPosition}/${questionList!!.size}"
    }

    private fun setColor(opt: Int, color: Int) {
        when (opt) {
            1 -> binding.opt1.background = ContextCompat.getDrawable(this, color)
            2 -> binding.opt2.background = ContextCompat.getDrawable(this, color)
            3 -> binding.opt3.background = ContextCompat.getDrawable(this, color)
            4 -> binding.opt4.background = ContextCompat.getDrawable(this, color)
        }
    }

    private fun questionStyle() {
        val quesStyle: ArrayList<TextView> = arrayListOf(
            binding.opt1,
            binding.opt2,
            binding.opt3,
            binding.opt4
        )

        for (op in quesStyle) {
            op.setTextColor(Color.parseColor("#555151"))
            op.background = ContextCompat.getDrawable(this, R.drawable.transparent)
            op.typeface = Typeface.DEFAULT
        }
    }

    private fun selectOption(view: TextView, opt: Int) {
        questionStyle()

        view.background = ContextCompat.getDrawable(this, R.drawable.selected_question_option)
        view.typeface = Typeface.DEFAULT_BOLD
        selectedOption = opt
    }
}
