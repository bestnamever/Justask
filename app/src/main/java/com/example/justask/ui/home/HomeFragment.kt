package com.example.juskask2.ui.home

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.juskask2.Model.Item
import com.example.juskask2.Service.SharedPreference
import com.example.juskask2.adapter.ItemListAdapter
import com.example.juskask2.ui.dialog.RaiseQuestion
import com.example.justask.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var listView: ListView
    private lateinit var swipe: SwipeRefreshLayout
    private lateinit var adapter: ItemListAdapter
    val db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        root.apply {
            swipe = findViewById(R.id.swipe)

            listView = this.findViewById(R.id.Itemlist)
            var list = mutableListOf<Item>()
            var docRef =
                db.collection("questions").orderBy("Time", Query.Direction.DESCENDING).get()
            docRef.addOnSuccessListener { documents ->
                for (document in documents) {
                    if (document != null) {
                        val question: String = document.getString("question_text").toString()
                        val question_des: String =
                            document.getString("question_des_text").toString()
                        list.add(Item(question, question_des))
                        adapter = ItemListAdapter(
                            requireActivity().applicationContext,
                            R.layout.item_list,
                            list
                        )
                        listView.adapter = adapter
                    } else {
                        Log.d("2", "NO")
                    }
                }
            }
                .addOnFailureListener { exception ->
                    Log.d("error", "fail", exception)
                }

            swipe.setProgressBackgroundColorSchemeColor(
                ContextCompat.getColor(
                    this.context,
                    R.color.black
                )
            )
            swipe.setColorSchemeColors(Color.WHITE)

            swipe.setOnRefreshListener {
                val ft = getParentFragmentManager().beginTransaction()
                ft.detach(this@HomeFragment).attach(this@HomeFragment).commit()
                Handler().postDelayed(Runnable {
                    swipe.isRefreshing = false
                }, 1000)
            }

            listView.setOnItemClickListener { parent, view, position, id ->
                val sharedPreference: SharedPreference = SharedPreference(this.context)
                sharedPreference.save("question", list.get(position).question)
                sharedPreference.save("question_des", list.get(position).question_des)
                view.findNavController().navigate(R.id.navigation_questions)
            }

            this.findViewById<Button>(R.id.RaisequestionButton).setOnClickListener {
                RaiseQuestion(this.context).show()
            }
        }

        return root
    }
}