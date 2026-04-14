package com.intprog.ebay

import android.content.Intent
import android.widget.Button
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login) // your login layout

        val btn = findViewById<Button>(R.id.button4)
        btn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Find the "Use email" button from your XML
        val loginBtn = findViewById<Button>(R.id.button)
        loginBtn.setOnClickListener {
            // Open the Sign In page (replace 'YourNextActivity' with your actual class name)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}