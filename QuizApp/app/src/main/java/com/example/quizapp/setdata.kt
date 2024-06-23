package com.example.quizapp

object SetData {
    val name:String="name"
    val score:String="score"
    fun questionData(): ArrayList<SetQuestion> {
        val questionsList: ArrayList<SetQuestion> = arrayListOf()

        // 1
        val que1 = SetQuestion(
            1, "Which of the following can be arranged into a 5-letter English word?",
            R.drawable.question,
            "R I L S A", "W Q R G S",
            "H R G S T", "M D K A L", 1
        )
        questionsList.add(que1)

        // 2
        val que2 = SetQuestion(
            2, "Which number should come next in the pattern? 37, 34, 31, 28",
            R.drawable.question2,
            "24", "27",
            "25", "21", 3
        )
        questionsList.add(que2)

        // 3
        val que3 = SetQuestion(
            3, "7, 10, 8, 11, 9, 12, ... What number should come next?",
            R.drawable.question3,
            "13", "11",
            "9", "10", 4
        )
        questionsList.add(que3)

        // 4
        val que4 = SetQuestion(
            4, "Which pair of numbers comes next 42 40, 38 35, 33 31, 28?",
            R.drawable.question4,
            "25 22", "26 23",
            "26 24", "25 23", 3
        )
        questionsList.add(que4)

        // 5
        val que5 = SetQuestion(
            5, "36 31, 29 24, 22 17, 15 which pair of numbers comes next",
            R.drawable.question5,
            "13 , 11", "13 , 8",
            "10 , 8", "12 , 7", 3
        )
        questionsList.add(que5)

        // 6
        val que6 = SetQuestion(
            6, "3 5 35 10 12 35 17 what next ?",
            R.drawable.question6,
            "19 35", "20 22",
            "19 24", "18 19", 1
        )
        questionsList.add(que6)

        // 7
        val que7 = SetQuestion(
            7, "What would be the next? Sock:foot:: gloves: ?",
            R.drawable.question2,
            "Hand", "Arm",
            "Fingers", "Boxing", 3
        )
        questionsList.add(que7)

        // 8
        val que8 = SetQuestion(
            8, "Choose the pair which has the same relation like as Joke: Laughter?",
            R.drawable.question2,
            "Death : Life", "Disease : Medicine",
            "Cry : Sorrow", "Miracle : Surprise", 4
        )
        questionsList.add(que8)

        // 9
        val que9 = SetQuestion(
            9, "if PIPE is written as 169165, Then what is the last digit of SWAN?",
            R.drawable.question3,
            "3", "4",
            "8", "9", 2
        )
        questionsList.add(que9)

        // 10
        val que10 = SetQuestion(
            10, "If PAISA:RUPEE:: ? : KILOMETER",
            R.drawable.question6,
            "Decametre", "Meter",
            "Centimeter", "Distance", 1
        )
        questionsList.add(que10)

        return questionsList
    }
}
