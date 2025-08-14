package com.example.blogapplication.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.blogapplication.SignandregistrationActivity

import com.example.blogapplication.databinding.ActivityWelcomeBinding // ✅ Add here

class WelcomeActivity : AppCompatActivity() {
    private val binding: ActivityWelcomeBinding by lazy {
        ActivityWelcomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val intent = Intent(this, SignandregistrationActivity::class.java)
            intent.putExtra("action", "login") // ✅ Correct method
            startActivity(intent)
        }

        binding.registerButton.setOnClickListener {
            val intent = Intent(this, SignandregistrationActivity::class.java)
            intent.putExtra("action", "register") // ✅ Correct method
            startActivity(intent)
        }

    }
}
