package com.example.justask

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.juskask2.Service.SharedPreference
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
                setOf(
                        R.id.navigation_home,
                        R.id.navigation_settings,
                        R.id.navigation_questions
                )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val sharedPreference: SharedPreference = SharedPreference(this.applicationContext)
        val currentuser = sharedPreference.getValueString("username")
        val db = FirebaseFirestore.getInstance()
        db.collection("invitation").whereEqualTo("TargetUser", currentuser).get()
                .addOnSuccessListener { document ->
                    sharedPreference.saveInt("invitations", document.size())
                }
        db.collection("invitation").whereEqualTo("TargetUser", currentuser)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.d("snapshot", "Listen failed.", e)
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        if (snapshot.size() > sharedPreference.getValueInt("invitations")!!) {
                            Toast.makeText(
                                    this.applicationContext,
                                    "You have a new invitation",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (snapshot.size() < sharedPreference.getValueInt("invitations")!!) {
                            sharedPreference.saveInt("invitations", snapshot.size())
                        }
                        Log.d("snapshot", "Current data: null")
                    }
                }
    }
}