package com.example.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Color.parseColor
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_quiz_questions.*

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private var mCurrentPosition: Int = 1
    private var mQuestionsList: ArrayList<Question>? = null
    private var mSelectedPosition: Int = 0
    private var mCorrectAnswers: Int = 0
    private var mUserName: String? = null
    private var submited = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        mQuestionsList = Constants.getQuestions()

        setQuestion()

        tvOptionOne.setOnClickListener(this)
        tvOptionTwo.setOnClickListener(this)
        tvOptionThree.setOnClickListener(this)
        tvOptionFour.setOnClickListener(this)

        btnSubmit.setOnClickListener(this)

    }

    private fun answerView(answer: Int, drawableView: Int){
        when(answer){
            1->{
                tvOptionOne.background = ContextCompat.getDrawable(this,drawableView)
            }
            2->{
                tvOptionTwo.background = ContextCompat.getDrawable(this,drawableView)
            }
            3->{
                tvOptionThree.background = ContextCompat.getDrawable(this,drawableView)
            }
            4->{
                tvOptionFour.background = ContextCompat.getDrawable(this,drawableView)
            }
        }
    }

    private fun setQuestion(){

        val question = mQuestionsList!![mCurrentPosition - 1]

        defualtOptionsView()

        if(mCurrentPosition==mQuestionsList!!.size){
            btnSubmit.text = "FINISH"
        }else{
            btnSubmit.text= "SUBMIT"
        }

        progressBar.progress = mCurrentPosition
        tvProgress.text = "$mCurrentPosition" + "/" + progressBar.max
        tvQue.text = question.question
        ivImage.setImageResource(question.image)
        tvOptionOne.text = question.optionOne
        tvOptionTwo.text = question.optionTwo
        tvOptionThree.text = question.optionThree
        tvOptionFour.text = question.optionFour
    }

    private fun defualtOptionsView(){
        val options = ArrayList<TextView>()
        options.add(0, tvOptionOne)
        options.add(1, tvOptionTwo)
        options.add(2, tvOptionThree)
        options.add(3, tvOptionFour)

        for(option in options){
            option.setTextColor(parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.defualt_option_boarded_bg
            )

        }
    }

    override fun onClick(v: View?) {

        when(v?.id){
            R.id.tvOptionOne ->{
                if(submited==false){
                    selectedOptionView(tvOptionOne, 1)
                }
            }
            R.id.tvOptionTwo ->{
                if(submited==false){
                    selectedOptionView(tvOptionTwo, 2)
                }
            }
            R.id.tvOptionThree ->{
                if(submited==false){
                    selectedOptionView(tvOptionThree, 3)
                }
            }
            R.id.tvOptionFour ->{
                if(submited==false){
                    selectedOptionView(tvOptionFour, 4)
                }
            }
            R.id.btnSubmit ->{
                if(mSelectedPosition == 0){
                    submited = false
                    mCurrentPosition++

                    when{
                        mCurrentPosition <= mQuestionsList!!.size ->{
                            setQuestion()
                        }else ->{

                        val intent = Intent(this, ResultActivity::class.java)
                        intent.putExtra(Constants.USER_NAME,mUserName)
                        intent.putExtra(Constants.CORRECT_ANSWERS,mCorrectAnswers)
                        intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList!!.size)
                        startActivity(intent)
                        finish()

                        }
                    }
                }else{
                    val question = mQuestionsList?.get(mCurrentPosition - 1)

                    if(question!!.correctAnswer != mSelectedPosition){
                        answerView(mSelectedPosition, R.drawable.wrong_option_boarded_bg)
                    }else{
                        mCorrectAnswers++
                    }
                    answerView(question.correctAnswer, R.drawable.correct_option_boarded_bg)

                    if(mCurrentPosition==mQuestionsList!!.size){
                        btnSubmit.text = "FINISH"
                    }else{
                        btnSubmit.text= "Go To Next Question"
                    }

                    mSelectedPosition =0
                    submited = true

                }


            }
        }

    }

    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int){
        defualtOptionsView()
        mSelectedPosition = selectedOptionNum

        tv.setTextColor(parseColor("#3634A3"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_boarded_bg
        )


    }
}