package com.intprog.ebay

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {

    private val viewModel: SearchViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("title") ?: ""
        val price = arguments?.getString("price") ?: ""

        view.findViewById<TextView>(R.id.titleText).text = title
        view.findViewById<TextView>(R.id.priceText).text = price

        val currentItem = Item(title, price)

        // 1. Setup Watchlist button (Matches your XML ID: btnAddToWatchlist)
        val btnWatchlist = view.findViewById<Button>(R.id.btnAddToWatchlist)
        btnWatchlist.setOnClickListener {
            viewModel.toggleWatchlist(currentItem)
            Toast.makeText(requireContext(), "Added to Watchlist!", Toast.LENGTH_SHORT).show()
        }

        // 2. Setup Add to Cart button (Matches your XML ID: btnAddToCart)
        val btnCart = view.findViewById<Button>(R.id.btnAddToCart)
        btnCart.setOnClickListener {
            // Reusing this for Saved logic as well so you can see it in your Profile
            viewModel.toggleSaved(currentItem)
            Toast.makeText(requireContext(), "Added to Cart & Saved!", Toast.LENGTH_SHORT).show()
        }

        // 3. Setup Buy It Now (Matches your XML ID: btnBuyNow)
        val btnBuy = view.findViewById<Button>(R.id.btnBuyNow)
        btnBuy.setOnClickListener {
            Toast.makeText(requireContext(), "Checking out...", Toast.LENGTH_SHORT).show()
        }
    }
}