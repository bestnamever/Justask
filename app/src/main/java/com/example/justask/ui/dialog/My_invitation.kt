package com.example.juskask2.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.ListView
import android.widget.TextView
import com.example.juskask2.Model.Item
import com.example.juskask2.Service.DatabaseMethod
import com.example.juskask2.Service.SharedPreference
import com.example.juskask2.adapter.ItemListAdapter
import com.example.justask.R
import com.google.firebase.firestore.FirebaseFirestore

class My_invitation(context: Context) : Dialog(context) {
    val db = FirebaseFirestore.getInstance()
    val databaseMethod: DatabaseMethod = DatabaseMethod()
    private lateinit var adapter: ItemListAdapter

    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.my_question_dialog)

        val sharedPreference: SharedPreference = SharedPreference(this.context)
        val My_question: TextView = findViewById(R.id.my_setting_label)
        val My_invitation: ListView = findViewById(R.id.my_settings_list)

        My_question.text = sharedPreference.getValueString("label")
        val currentUser: String = sharedPreference.getValueString("username").toString()
        var list = mutableListOf<Item>()

        var docRef = db.collection("invitation").whereEqualTo("TargetUser", currentUser).get()
        docRef.addOnSuccessListener { documents ->
            for (document in documents) {
                if (document != null) {
                    Log.d("myinvitation", "${document.data}")
                    list.add(
                        Item(
                            document.getString("Question").toString(),
                            document.getString("InvitedBy").toString()
                        )
                    )
                    adapter = ItemListAdapter(
                        context,
                        R.layout.item_list,
                        list
                    )
                    My_invitation.adapter = adapter

                }
            }
        }

        My_invitation.setOnItemClickListener { parent, view, position, id ->
            val question = list.get(position).question
            val InvitedBy = list.get(position).question_des
            val inviRead = true
            val inviInfo = HashMap<String,Any>()
            Log.d("test123", list.get(position).question)
            inviInfo["Viewed"] = inviRead
            databaseMethod.dismissinvitation(question + " is invited by " + InvitedBy + " to " + currentUser)
            dismiss()
            Answer_dia(this.context).show()
        }
    }
}