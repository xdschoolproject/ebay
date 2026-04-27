package com.intprog.ebay

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchResultsFragment : Fragment(R.layout.fragment_search_results) {

    private lateinit var adapter: SearchAdapter
    private val viewModel: SearchViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val resultCount = view.findViewById<TextView>(R.id.resultCount)

        // 1. Initialize Adapter
        adapter = SearchAdapter(
            items = emptyList(),
            displayMode = SearchAdapter.MODE_SEARCH
        ) { item -> viewModel.addToRecentlyViewed(item)

            val bundle = Bundle().apply {
                putString("title", item.title)
                putString("price", item.price)
                putInt("imageResId", item.imageResId)
            }
            val fragment = ProductDetailsFragment().apply { arguments = bundle }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // 2. Extract the mode/query from arguments
        val query = arguments?.getString("query") ?: ""

        // 3. Collect the correct data flow
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                val flowToObserve = when (query) {
                    "WATCHLIST_MODE" -> {
                        resultCount.text = "Watchlist"
                        viewModel.watchlist
                    }
                    "SAVED_MODE" -> {
                        resultCount.text = "Saved Items"
                        viewModel.savedItems
                    }
                    else -> {
                        viewModel.updateQuery(query)
                        viewModel.searchResults
                    }
                }

                flowToObserve.collectLatest { items ->
                    adapter.updateList(items)

                    if (query != "WATCHLIST_MODE" && query != "SAVED_MODE") {
                        resultCount.text = "${items.size} results for \"$query\""
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(query: String): SearchResultsFragment {
            val fragment = SearchResultsFragment()
            val args = Bundle()
            args.putString("query", query)
            fragment.arguments = args
            return fragment
        }
    }
}