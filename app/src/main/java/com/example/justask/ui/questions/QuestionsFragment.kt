package com.example.juskask2.ui.questions

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.juskask2.Model.AnswerList
import com.example.juskask2.Service.SharedPreference
import com.example.juskask2.adapter.AnswerListAdapter
import com.example.juskask2.ui.dialog.Answer_dia
import com.example.juskask2.ui.dialog.Invite
import com.example.juskask2.ui.home.QuestionsViewModel
import com.example.justask.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class QuestionsFragment : Fragment() {

    private lateinit var questionsViewModel: QuestionsViewModel
    private lateinit var qName: String
    private lateinit var qDes: String
    private lateinit var listView: ListView
    private lateinit var swipe: SwipeRefreshLayout
    private lateinit var adapter: AnswerListAdapter
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        questionsViewModel =
            ViewModelProvider(this).get(QuestionsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_questions, container, false)
        root.apply {
            val qN: TextView = root.findViewById(R.id.question_qn)
            val qD: TextView = root.findViewById(R.id.question_qd)
            qD.text =""

            val sharedPreference: SharedPreference = SharedPreference(this.context)
            val currentuser = sharedPreference.getValueString("username").toString()
            if (sharedPreference.getValueString("question").toString().equals("null"))
                qName = "No question selected"
            else
                qName = sharedPreference.getValueString("question").toString()
            db.collection("questions").document(qName).get().addOnSuccessListener {
                document -> qDes = document.getString("question_des_text").toString()
                qD.text = qDes
            }
            qN.text = qName


            this.findViewById<Button>(R.id.answer_button).setOnClickListener {
                Answer_dia(this.context).show()
            }
            this.findViewById<Button>(R.id.invite).setOnClickListener {
                Invite(this.context).show()
            }

            swipe = findViewById(R.id.swipe)
            listView = this.findViewById(R.id.answerlist)
            var list = mutableListOf<AnswerList>()

            fun readanswer(){
                var docRef = db.collection("questions").document(qName).collection("answers")
                .orderBy("Time", Query.Direction.DESCENDING).get()
                docRef.addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document != null) {
                            val answer: String = document.getString("answer_text").toString()
                            val answerer: String = document.getString("answerer").toString()
                            val islike:Boolean = document.getBoolean("is liked by" + currentuser) == true
                            Log.d("search_fav",islike.toString())
                            list.add(AnswerList(answerer, answer,islike))
                            adapter =
                                AnswerListAdapter(
                                    requireActivity().applicationContext,
                                    R.layout.answer_list,
                                    list
                                )
                            listView.adapter = adapter
                        } else {
                            Log.d("2", "NO")
                        }
                    }
                }
            }
            readanswer()


            swipe.setProgressBackgroundColorSchemeColor(
                ContextCompat.getColor(
                    this.context,
                    R.color.black
                )
            )
            swipe.setColorSchemeColors(Color.WHITE)

            swipe.setOnRefreshListener {
                val ft = getParentFragmentManager().beginTransaction()
                ft.detach(this@QuestionsFragment).attach(this@QuestionsFragment).commit()
                Handler().postDelayed(Runnable {
                    swipe.isRefreshing = false
                }, 1000)
            }

        }
        return root
    }
}