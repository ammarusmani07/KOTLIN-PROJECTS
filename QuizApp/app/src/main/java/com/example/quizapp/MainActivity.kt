package com.example.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.quizapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_FULLSCREEN
        binding.button.setOnClickListener {

            if(binding.input.text.toString().isEmpty()||binding.input2.text.toString().isEmpty())
            {
                Toast.makeText(this,"You Already failed your 1st test read the above Statement", Toast.LENGTH_LONG).show()
            }
            else{

                var  intent = Intent(this,QuestionActivity::class.java)
                val name = binding.input.text.toString()
                intent.putExtra(SetData.name, name)
                startActivity(intent)
                finish()
            }
        }
    }
    }
