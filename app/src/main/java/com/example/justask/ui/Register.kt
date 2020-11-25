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

class Register:AppCompatActivity() {
    private lateinit var firebaseAuth:FirebaseAuth
    private var db:DatabaseMethod = DatabaseMethod()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        this.findViewById<Button>(R.id.Register_button).setOnClickListener(){
            val username = findViewById<EditText>(R.id.Register_username).text.toString()
            val email = findViewById<EditText>(R.id.Register_email).text.toString()
            val password = findViewById<EditText>(R.id.Register_password).text.toString()
            val user = HashMap<String, Any>()
            user["name"] = username

            val sharedPreference: SharedPreference =SharedPreference(this)

            if(username.isEmpty()||email.isEmpty()||password.isEmpty()){
                Toast.makeText(this,"Please enter username/email/password",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            db.setNewUser(email,user)
            sharedPreference.save("username",username)
            sharedPreference.save("email",email)


            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                if (!it.isSuccessful) Toast.makeText(this,"Fail to register",Toast.LENGTH_SHORT).show()
                else {
                    Toast.makeText(this, "You are registered", Toast.LENGTH_SHORT).show()
                    Log.d("Register", "register with uid: ${it.result?.user?.uid}")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }

        }

        this.findViewById<TextView>(R.id.gotoLogin).setOnClickListener(){
            finish()
        }
    }
}