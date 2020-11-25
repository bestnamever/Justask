package com.example.juskask2.ui.dialog


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.juskask2.Service.DatabaseMethod
import com.example.juskask2.Service.SharedPreference
import com.example.justask.R

class Answer_dia(context: Context) : Dialog(context) {
    private var db: DatabaseMethod = DatabaseMethod()


    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.answer_dialog)

        val answer: EditText = findViewById(R.id.answer_dia)
        val button: Button = findViewById(R.id.submit_ad)
        button.setOnClickListener {
            var answer_text:String = answer.text.toString()
            val sharedPreference: SharedPreference = SharedPreference(this.context)
            val currentquestion : String = sharedPreference.getValueString("question").toString()
            val currentUser: String = sharedPreference.getValueString("username").toString()
            val currentUserEmail:String = sharedPreference.getValueString("email").toString()
            val answerInfo = HashMap<String, Any>()
            val currentTimestamp = System.currentTimeMillis().toString()
            val islike = false
            answerInfo["question_text"]=currentquestion
            answerInfo["answerer"] = currentUser
            answerInfo["answer_text"] = answer_text
            answerInfo["Time"] = currentTimestamp
            answerInfo["is liked by" + currentUser] = islike
            if(answer_text.isEmpty())
                Toast.makeText(it.context, "Please submit with an answer", Toast.LENGTH_SHORT).show()
            else {
                db.setNewAnswerToquestion(currentquestion,currentUser,answerInfo)
                db.setNewAnswerToUser(currentUserEmail,currentTimestamp,answerInfo)
                Toast.makeText(it.context, "Answer has been submitted", Toast.LENGTH_SHORT).show()
                dismiss()
            }

        }
    }
}