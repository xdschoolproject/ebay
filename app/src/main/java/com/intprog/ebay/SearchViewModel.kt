package com.intprog.ebay

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchViewModel : ViewModel() {

    private val fullList = listOf(
        Item("iPhone 13", "$800", R.drawable.iphone13),
        Item("iPhone 13 128GB Black", "$800", R.drawable.iphone13_black),
        Item("iPhone 13 256GB Blue", "$900", R.drawable.iphone13_blue),
        Item("iPhone 14 128GB Starlight", "$950", R.drawable.iphone14_starlight)
    )

    // 1. Existing Search Flows
    private val _searchResults = MutableStateFlow(fullList)
    val searchResults: StateFlow<List<Item>> = _searchResults.asStateFlow()

    private val _recentSearches = MutableStateFlow<List<Item>>(emptyList())
    val recentSearches: StateFlow<List<Item>> = _recentSearches.asStateFlow()

    // 🟢 NEW: Watchlist and Saved Flows
    private val _watchlist = MutableStateFlow<List<Item>>(emptyList())
    val watchlist: StateFlow<List<Item>> = _watchlist.asStateFlow()

    private val _savedItems = MutableStateFlow<List<Item>>(emptyList())
    val savedItems: StateFlow<List<Item>> = _savedItems.asStateFlow()

    private val _recentlyViewed = MutableStateFlow<List<Item>>(emptyList())
    val recentlyViewed: StateFlow<List<Item>> = _recentlyViewed.asStateFlow()

    // 2. Search Logic
    fun updateQuery(query: String) {
        val cleanQuery = query.trim().lowercase()
        if (cleanQuery.isBlank()) {
            _searchResults.value = fullList
            return
        }
        val keywords = cleanQuery.split("\\s+".toRegex())
        _searchResults.value = fullList.filter { item ->
            val titleLower = item.title.lowercase()
            keywords.all { keyword -> titleLower.contains(keyword) }
        }
    }

    // 🟢 NEW: Watchlist Logic (Toggle style)
    fun toggleWatchlist(item: Item) {
        val current = _watchlist.value.toMutableList()
        if (current.any { it.title == item.title }) {
            current.removeAll { it.title == item.title }
        } else {
            current.add(0, item)
        }
        _watchlist.value = current
    }

    // 🟢 NEW: Saved Logic (Toggle style)
    fun toggleSaved(item: Item) {
        val current = _savedItems.value.toMutableList()
        if (current.any { it.title == item.title }) {
            current.removeAll { it.title == item.title }
        } else {
            current.add(0, item)
        }
        _savedItems.value = current
    }

    // 3. History Logic
    fun addSearch(query: String) {
        val cleanQuery = query.trim()
        if (cleanQuery.isEmpty()) return

        val currentHistory = _recentSearches.value.toMutableList()
        currentHistory.removeAll { it.title.equals(cleanQuery, ignoreCase = true) }

        currentHistory.add(0, Item(cleanQuery, ""))

        if (currentHistory.size > 10) currentHistory.removeAt(currentHistory.lastIndex)
        _recentSearches.value = currentHistory
    }

    fun clearRecent() {
        _recentSearches.value = emptyList()
    }

    // 🟢 NEW: Recently Viewed Logic
    fun addToRecentlyViewed(item: Item) {
        val current = _recentlyViewed.value.toMutableList()

        // 1. Remove if already exists so we can move it to the front (no duplicates)
        current.removeAll { it.title == item.title }

        // 2. Add to index 0 (the "Recent" spot)
        current.add(0, item)

        // 3. Limit to 10 items to keep it clean
        if (current.size > 10) current.removeAt(current.lastIndex)

        // 4. Push update to the flow
        _recentlyViewed.value = current
    }
}