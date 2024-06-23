package com.example.quizapp

data class SetQuestion(
    var id: Int,
    var question: String,
    var img: Int, // Change the type to Int for resource ID
    var one: String,
    var tw0: String,
    var three: String,
    var four: String,
    var ans: Int
)
