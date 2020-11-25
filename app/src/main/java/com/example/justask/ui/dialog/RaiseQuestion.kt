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

class RaiseQuestion(context: Context) : Dialog(context) {
    private var db: DatabaseMethod = DatabaseMethod()

    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.raisequestion_dialog)

        val question: EditText = findViewById(R.id.question_name)
        val question_des: EditText = findViewById(R.id.question_text)
        val button: Button = findViewById(R.id.submit_qd)

        button.setOnClickListener {
            val question_text: String = question.text.toString()
            val question_des_text: String = question_des.text.toString()
            val sharedPreference: SharedPreference = SharedPreference(this.context)
            val currentUser: String = sharedPreference.getValueString("username").toString()
            val questionInfo = HashMap<String, Any>()
            val currentTimestamp = System.currentTimeMillis()
            questionInfo["question_text"] = question_text
            questionInfo["question_des_text"] = question_des_text
            questionInfo["RaisedBy"] = currentUser
            questionInfo["Time"] = currentTimestamp
            if (question_text.isEmpty())
                Toast.makeText(it.context, "Please set a question", Toast.LENGTH_SHORT).show()
            else {
                db.setNewQuestion(question_text,questionInfo)
                Toast.makeText(it.context, "question has been raised", Toast.LENGTH_SHORT).show()
                dismiss()
            }

        }
    }
}