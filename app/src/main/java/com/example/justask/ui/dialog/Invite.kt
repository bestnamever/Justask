package com.example.juskask2.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.juskask2.Model.User
import com.example.juskask2.Service.DatabaseMethod
import com.example.juskask2.Service.SharedPreference
import com.example.justask.R
import com.google.firebase.firestore.FirebaseFirestore


class Invite(context: Context) : Dialog(context) {
    private lateinit var radioButton: RadioButton
    val db = FirebaseFirestore.getInstance()
    val databaseMethod: DatabaseMethod = DatabaseMethod()


    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.invite_dialog)

        val radiogroup: RadioGroup = findViewById(R.id.radioGroup)
        val button: Button = findViewById(R.id.submit_id)

        val sharedPreference: SharedPreference = SharedPreference(context)
        val question: String = sharedPreference.getValueString("question").toString()
        val currentUser: String = sharedPreference.getValueString("username").toString()
        val viewed: Boolean = false


        val invitation = HashMap<String, Any>()
        var list = mutableListOf<User>()
        var docRef = db.collection("users").get()
        docRef.addOnSuccessListener { documents ->
            for (document in documents) {
                if (document != null) {
                    val user: String = document.getString("name").toString()
                    list.add(User(user))
                } else {
                    Log.d("2", "NO")
                }
            }
            for (i in 0 until list.size) {
                val radioButtonView = RadioButton(this.context)
                if(!list[i].user.equals(currentUser)) {
                    radioButtonView.setText(list[i].user)
                    radioButtonView.setTextColor(R.color.black)
                    radioButtonView.setTextSize(20.0F)
                    radiogroup.addView(radioButtonView)
                }
            }
        }

        button.setOnClickListener {
            if (radiogroup.checkedRadioButtonId == -1) {
                Toast.makeText(it.context, "No user selected", Toast.LENGTH_SHORT).show()
            } else {
                val intSelectButton: Int = radiogroup!!.checkedRadioButtonId
                radioButton = findViewById(intSelectButton)
                val targetUser: String = radioButton.text.toString()
                invitation["InvitedBy"] = currentUser
                invitation["TargetUser"] = targetUser
                invitation["Viewed"] = viewed
                invitation["Question"] = question
                Log.d("inviatition", question + currentUser)
                databaseMethod.newinvitation(
                    question + " is invited by " + currentUser + " to " + targetUser,
                    invitation
                )
                Toast.makeText(
                    it.context,
                    "User " + radioButton.text + " is invited",
                    Toast.LENGTH_SHORT
                ).show()

                dismiss()
            }
        }
    }
}