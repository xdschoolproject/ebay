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

    private val viewModel: SearchViewModel by activityViewModels()
    private lateinit var recentAdapter: SearchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recentlyViewedSection = view.findViewById<LinearLayout>(R.id.recentlyViewedSection)
        val loginRegisterSection = view.findViewById<LinearLayout>(R.id.loginRegisterSection)
        val recentRecyclerView = view.findViewById<RecyclerView>(R.id.recentRecyclerView)

        // 🟢 INITIALIZE ADAPTER WITH MINI MODE
        recentAdapter = SearchAdapter(
            items = emptyList(),
            displayMode = SearchAdapter.MODE_MINI_CARD
        ) { item ->
            val bundle = Bundle().apply {
                putString("title", item.title)
                putString("price", item.price)
            }
            val fragment = ProductDetailsFragment().apply { arguments = bundle }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        // 🟢 SET HORIZONTAL ORIENTATION
        recentRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recentRecyclerView.adapter = recentAdapter

        val isUserLoggedIn = requireActivity().intent.getBooleanExtra("isLoggedIn", false)

        if (isUserLoggedIn) {
            loginRegisterSection.visibility = View.GONE

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.recentlyViewed.collect { items ->
                        if (items.isNotEmpty()) {
                            recentlyViewedSection.visibility = View.VISIBLE
                            recentAdapter.updateList(items)
                        } else {
                            recentlyViewedSection.visibility = View.GONE
                        }
                    }
                }
            }
        } else {
            recentlyViewedSection.visibility = View.GONE
            loginRegisterSection.visibility = View.VISIBLE
        }
    }
}