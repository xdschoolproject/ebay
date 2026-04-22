package com.intprog.ebay

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class SearchFragment : Fragment(R.layout.search) {

    private lateinit var recentSection: LinearLayout
    private lateinit var savedSection: LinearLayout
    private lateinit var recentRecycler: RecyclerView
    private lateinit var recentEmpty: TextView
    private lateinit var adapter: SearchAdapter

    private val viewModel: SearchViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Views
        recentSection = view.findViewById(R.id.recentSection)
        savedSection = view.findViewById(R.id.savedSection)
        recentRecycler = view.findViewById(R.id.recentRecycler)
        recentEmpty = view.findViewById(R.id.recentEmpty)
        val buttonRecent = view.findViewById<TextView>(R.id.buttonRecent)
        val buttonSaved = view.findViewById<TextView>(R.id.buttonSaved)
        val searchView = view.findViewById<SearchView>(R.id.searchBar)
        val buttonClearRecent = view.findViewById<TextView>(R.id.buttonClearRecent)

        // Tab Switching Logic
        buttonRecent.setOnClickListener {
            recentSection.visibility = View.VISIBLE
            savedSection.visibility = View.GONE
        }

        buttonSaved.setOnClickListener {
            recentSection.visibility = View.GONE
            savedSection.visibility = View.VISIBLE
        }

        adapter = SearchAdapter(
            items = emptyList(),
            displayMode = SearchAdapter.MODE_HISTORY // 🟢 This replaces isHistoryMode = true
        ) { item ->
            // Clicking a recent search should perform the search again
            navigateToResults(item.title)
        }

        recentRecycler.layoutManager = LinearLayoutManager(requireContext())
        recentRecycler.adapter = adapter

        // Observe the recentSearches Flow
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.recentSearches.collect { history ->
                adapter.updateList(history)
                recentEmpty.visibility = if (history.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        // Search Logic
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.addSearch(it)
                    navigateToResults(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.updateQuery(newText ?: "")
                return true
            }
        })

        buttonClearRecent.setOnClickListener {
            viewModel.clearRecent()
        }
    }

    // Helper function to avoid repeating code
    private fun navigateToResults(query: String) {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                SearchResultsFragment.newInstance(query)
            )
            .addToBackStack(null)
            .commit()
    }
}