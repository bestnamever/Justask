package com.example.juskask2.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.ListView
import android.widget.TextView
import com.example.juskask2.Model.Item
import com.example.juskask2.Service.SharedPreference
import com.example.juskask2.adapter.ItemListAdapter
import com.example.justask.R
import com.google.firebase.firestore.FirebaseFirestore

class My_question_dia(context: Context) : Dialog(context) {
    val db = FirebaseFirestore.getInstance()
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
        val My_question_list: ListView = findViewById(R.id.my_settings_list)

        My_question.text = sharedPreference.getValueString("label")
        var list = mutableListOf<Item>()

        var docRef = db.collection("questions")
            .whereEqualTo("RaisedBy", sharedPreference.getValueString("username"))
        docRef.get().addOnSuccessListener { documents ->
            for (document in documents) {
                if (document != null) {
                    Log.d("myquestionlist", "${document.data}")
                    list.add(
                        Item(
                            document.getString("question_text").toString(),
                            document.getString("question_des_text").toString()
                        )
                    )
                    adapter = ItemListAdapter(
                        context,
                        R.layout.item_list,
                        list
                    )
                    My_question_list.adapter = adapter
                } else {
                    Log.d("2", "NO")
                }
            }
        }
    }
}
