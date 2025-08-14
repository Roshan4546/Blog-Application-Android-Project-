package com.example.blogapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.blogapplication.databinding.ActivitySignandregistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignandregistrationActivity : AppCompatActivity() {
    private val binding: ActivitySignandregistrationBinding by lazy {
        ActivitySignandregistrationBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Enable offline persistence
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val action = intent.getStringExtra("action")
        if (action == "login") {
            binding.loginEmailAddress.visibility = View.VISIBLE
            binding.loginPasword.visibility = View.VISIBLE
            binding.loginButton.visibility = View.VISIBLE
            binding.registerButton.isEnabled = false
            binding.registerButton.alpha = 0.5f
            binding.cardView.visibility = View.GONE
            binding.registerName.visibility = View.GONE
            binding.registerEmail.visibility = View.GONE
            binding.registerPassword.visibility = View.GONE
            binding.registerNewhere.isEnabled = false
            binding.registerNewhere.alpha = 0.5f
        } else if (action == "register") {
            binding.loginButton.isEnabled = false
            binding.loginButton.alpha = 0.5f

            binding.registerButton.setOnClickListener {
                val registerName = binding.registerName.text.toString()
                val registerEmail = binding.registerEmail.text.toString()
                val registerPassword = binding.registerPassword.text.toString()

                if (registerName.isEmpty() || registerEmail.isEmpty() || registerPassword.isEmpty()) {
                    Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
                } else {
                    auth.createUserWithEmailAndPassword(registerEmail, registerPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = task.result?.user
                                if (user != null) {
                                    val userRef: DatabaseReference = database.getReference("users")
                                    val userId: String = user.uid
                                    val userData = com.example.blogapplication.Model.UserData(registerName, registerEmail)
                                    userRef.child(userId).setValue(userData)
                                        .addOnSuccessListener {
                                            Log.d("TAG", "onCreate : data saved")
                                            Toast.makeText(this, "User Registered and Data Saved Successfully", Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e("TAG", "onCreate : Error saving data ${e.message}")
                                            Toast.makeText(this, "Failed to save user data: ${e.message}", Toast.LENGTH_LONG).show()
                                        }
                                } else {
                                    Log.e("TAG", "onCreate : User is null after registration")
                                    Toast.makeText(this, "Registration succeeded but user data is unavailable", Toast.LENGTH_LONG).show()
                                }
                            } else {
                                Log.e("TAG", "onCreate : Registration failed - ${task.exception?.message}")
                                Toast.makeText(this, "User Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }
}