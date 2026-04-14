package com.intprog.ebay

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val exitBtn = findViewById<ImageButton>(R.id.btnExit)
        exitBtn.setOnClickListener {
            finish() // Closes this screen, showing whatever was behind it
        }

        val emailField = findViewById<EditText>(R.id.editEmail)
        val passwordField = findViewById<EditText>(R.id.editPassword)
        val signInBtn = findViewById<Button>(R.id.btnSignInSubmit)

        signInBtn.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                // Show a quick popup message if they forgot to type something
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else {
                // 1. Create the Intent
                val intent = Intent(this, MainActivity::class.java)

                // 2. "Pack" the data (Key: "USER_EMAIL", Value: the actual email string)
                intent.putExtra("USER_EMAIL", email)

                // 3. Set isLoggedIn to to
                intent.putExtra("isLoggedIn", true)

                // 4. Send it and close this screen
                startActivity(intent)
                finish()
            }
        }
    }
}