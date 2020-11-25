package com.example.justask.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.juskask2.Service.DatabaseMethod
import com.example.juskask2.Service.SharedPreference
import com.example.justask.MainActivity
import com.example.justask.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Login:AppCompatActivity() {
    private var databaseMethod: DatabaseMethod = DatabaseMethod()
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        this.findViewById<Button>(R.id.Login_button).setOnClickListener() {
            val email = findViewById<EditText>(R.id.Login_username).text.toString()
            val password = findViewById<EditText>(R.id.Login_password).text.toString()
            val sharedPreference: SharedPreference = SharedPreference(this)
            sharedPreference.save("email",email)
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email/password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var docRef = db.collection("users").document(email)
            docRef.get()
                .addOnSuccessListener { document ->
                    if(document != null){
                        document.getString("name")?.let { it1 ->
                            sharedPreference.save("username",
                                it1
                            )
                        }
                    } else {
                        Log.d("2","NO")
                    }
                }
                .addOnFailureListener{
                        exception -> Log.d("error","fail",exception)
                }
            //sharedPreference.save("username",username)

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) Toast.makeText(this, "Fail to login", Toast.LENGTH_SHORT)
                        .show()
                    else {
                        Toast.makeText(this, "Logined", Toast.LENGTH_SHORT).show()
                        Log.d("Login", "login with uid: ${it.result?.user?.uid}")
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }

                }
        }

        this.findViewById<TextView>(R.id.gotoRegister).setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}
