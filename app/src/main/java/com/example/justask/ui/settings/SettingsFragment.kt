package com.example.juskask2.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.juskask2.Service.SharedPreference
import com.example.juskask2.ui.dialog.My_answer_dia
import com.example.juskask2.ui.dialog.My_fav_answer_dia
import com.example.juskask2.ui.dialog.My_invitation
import com.example.juskask2.ui.dialog.My_question_dia
import com.example.justask.R
import com.example.justask.ui.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        root.apply {
            val logout: Button = root.findViewById(R.id.setting_logout)
            val myanswer: Button = root.findViewById(R.id.setting_answers)
            val myfav: Button = root.findViewById(R.id.setting_fav)
            val myinvite: Button = root.findViewById(R.id.setting_invite)
            val myquestion: Button = root.findViewById(R.id.setting_myquestion)
            val sharedPreference = SharedPreference(this.context)
            this.findViewById<TextView>(R.id.my_username).text =
                sharedPreference.getValueString("username")
            db.collection("invitation").whereEqualTo("TargetUser",sharedPreference.getValueString("username")).get().addOnSuccessListener {
                documents ->
                for(document in documents) {
                    if (documents != null) {
                        val shake: Animation = AnimationUtils.loadAnimation(context, R.anim.shake)
                        myinvite.startAnimation(shake)
                    }
                }
            }
            logout.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                sharedPreference.clearSharedPreference()
                val intent = Intent(it.context, Login::class.java)
                startActivity(intent)
            }
            myquestion.setOnClickListener {
                sharedPreference.save("label", myquestion.text as String)
                My_question_dia(this.context).show()
            }
            myanswer.setOnClickListener {
                sharedPreference.save("label", myanswer.text as String)
                My_answer_dia(this.context).show()
            }
            myfav.setOnClickListener {
                sharedPreference.save("label", myfav.text as String)
                My_fav_answer_dia(this.context).show()
            }
            myinvite.setOnClickListener {
                sharedPreference.save("label", myinvite.text as String)
                My_invitation(this.context).show()
            }
        }

        return root
    }
}