package com.intprog.ebay

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.home) {

    // Shared ViewModel across Activity → used to store and share data (like recently viewed items)
    private val viewModel: SearchViewModel by activityViewModels()

    // Adapter handles how list items are displayed inside RecyclerView
    private lateinit var recentAdapter: SearchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get references to UI components from layout
        val recentlyViewedSection = view.findViewById<LinearLayout>(R.id.recentlyViewedSection)
        val loginRegisterSection = view.findViewById<LinearLayout>(R.id.loginRegisterSection)
        val recentRecyclerView = view.findViewById<RecyclerView>(R.id.recentRecyclerView)

        // Setup RecyclerView (layout + adapter + click behavior)
        setupRecyclerView(recentRecyclerView)

        // Read login state passed from previous Activity using Intent
        // Intent = "data carrier" when navigating between screens
        val isUserLoggedIn = requireActivity()
            .intent
            .getBooleanExtra("isLoggedIn", false)

        // Decide which UI to show based on login state
        if (isUserLoggedIn) {
            showLoggedInUI(loginRegisterSection, recentlyViewedSection)
        } else {
            showLoggedOutUI(loginRegisterSection, recentlyViewedSection)
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {

        // Initialize adapter with:
        // - empty list (data will come later from ViewModel)
        // - display mode (mini card layout)
        // - click listener (what happens when user clicks an item)
        recentAdapter = SearchAdapter(
            items = emptyList(),
            displayMode = SearchAdapter.MODE_MINI_CARD
        ) { item ->

            // When user clicks an item → navigate to ProductDetailsFragment
            // Bundle is used to pass data between fragments
            val fragment = ProductDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString("title", item.title)
                    putString("price", item.price)
                }
            }

            // Replace current fragment and add to back stack (so user can go back)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        // LayoutManager defines how items are arranged (horizontal scrolling list)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Attach adapter to RecyclerView
        recyclerView.adapter = recentAdapter
    }

    private fun showLoggedInUI(
        loginSection: LinearLayout,
        recentSection: LinearLayout
    ) {
        // Hide login/register UI since user is already logged in
        loginSection.visibility = View.GONE

        // Start collecting data from ViewModel
        // lifecycleScope ensures coroutine is tied to Fragment lifecycle
        viewLifecycleOwner.lifecycleScope.launch {

            // repeatOnLifecycle ensures collection only happens when UI is visible (STARTED)
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Collect data from StateFlow (reactive stream)
                viewModel.recentlyViewed.collect { items ->

                    // If there are recently viewed items → show section and update UI
                    if (items.isNotEmpty()) {
                        recentSection.visibility = View.VISIBLE
                        recentAdapter.updateList(items)
                    } else {
                        // If no data → hide section
                        recentSection.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun showLoggedOutUI(
        loginSection: LinearLayout,
        recentSection: LinearLayout
    ) {
        // Show login/register UI
        loginSection.visibility = View.VISIBLE

        // Hide recently viewed (since user is not logged in)
        recentSection.visibility = View.GONE
    }
}