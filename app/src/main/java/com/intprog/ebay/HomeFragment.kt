package com.intprog.ebay

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import android.widget.LinearLayout

class HomeFragment : Fragment(R.layout.home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recentlyViewedSection = view.findViewById<LinearLayout>(R.id.recentlyViewedSection)
        val loginRegisterSection = view.findViewById<LinearLayout>(R.id.loginRegisterSection)


        val isUserLoggedIn = requireActivity().intent.getBooleanExtra("isLoggedIn", false)

        if (isUserLoggedIn) {
            recentlyViewedSection.visibility = View.VISIBLE
            loginRegisterSection.visibility = View.GONE
        } else {
            recentlyViewedSection.visibility = View.GONE
            loginRegisterSection.visibility = View.VISIBLE
        }
    }
}