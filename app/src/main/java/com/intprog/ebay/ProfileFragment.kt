package com.intprog.ebay

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment(R.layout.profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the Sign in layout in profile.xml
        val signInLayout = view.findViewById<LinearLayout>(R.id.signInLayout)
        signInLayout.setOnClickListener {
            // Open the login screen
            val intent = Intent(requireContext(), SignInActivity::class.java)
            startActivity(intent)
        }

        // 1. Find the TextView by its ID
        val usernameTextView = view.findViewById<TextView>(R.id.txtUsername)

        // 2. Reach into MainActivity to get the 'userEmail' you stored there
        val email = (activity as? MainActivity)?.userEmail

        // 3. If the email isn't empty, display it!
        if (!email.isNullOrEmpty()) {
            usernameTextView.text = email
        }
    }
}