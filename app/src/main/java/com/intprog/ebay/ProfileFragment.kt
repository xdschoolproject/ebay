package com.intprog.ebay

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
// ✅ ADD THIS IMPORT
import com.google.android.material.card.MaterialCardView

class ProfileFragment : Fragment(R.layout.profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sign in Logic
        val signInLayout = view.findViewById<LinearLayout>(R.id.signInLayout)
        signInLayout.setOnClickListener {
            val intent = Intent(requireContext(), SignInActivity::class.java)
            startActivity(intent)
        }

        // Display Username/Email
        val usernameTextView = view.findViewById<TextView>(R.id.txtUsername)
        val email = (activity as? MainActivity)?.userEmail

        if (!email.isNullOrEmpty()) {
            usernameTextView.text = email
        }

        // ✅ WATCHLIST BUTTON
        val watchlistBtn = view.findViewById<MaterialCardView>(R.id.watchlistCard)
        watchlistBtn.setOnClickListener {
            val fragment = SearchResultsFragment.newInstance("WATCHLIST_MODE")
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        // ✅ SAVED BUTTON
        val savedBtn = view.findViewById<MaterialCardView>(R.id.savedCard)
        savedBtn.setOnClickListener {
            val fragment = SearchResultsFragment.newInstance("SAVED_MODE")
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}