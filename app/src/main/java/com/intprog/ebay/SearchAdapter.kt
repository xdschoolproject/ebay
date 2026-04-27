package com.intprog.ebay

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * A multi-purpose adapter used to display items in different UI states (Search, History, Mini-Cards).
 * @param items Initial dataset to be rendered.
 * @param displayMode Determines which layout and logic branch the adapter should use.
 * @param onClick Optional lambda expression to handle user interactions with an item.
 */
class SearchAdapter(
    private var items: List<Item>,
    private val displayMode: Int = MODE_SEARCH,
    private val onClick: ((Item) -> Unit)? = null
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    /**
     * View constants to maintain consistency across the app's UI layers.
     */
    companion object {
        const val MODE_SEARCH = 0      // Vertical listings with full details
        const val MODE_HISTORY = 1     // Simplified text-only recent queries
        const val MODE_MINI_CARD = 2   // Horizontal carousel-style layouts
    }

    /**
     * ViewHolder pattern to cache View references and minimize CPU-intensive findViewById calls.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.itemTitle)
        val price: TextView? = view.findViewById(R.id.itemPrice)
        val productImage: ImageView? = view.findViewById(R.id.itemImage)
    }

    /**
     * Inflates the appropriate XML resource based on the initialized [displayMode].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = when (displayMode) {
            MODE_HISTORY -> R.layout.item_recent_search
            MODE_MINI_CARD -> R.layout.item_recent
            else -> R.layout.item_search
        }

        val view = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)

        return ViewHolder(view)
    }

    /**
     * Populates the ViewHolder with data from the specific [Item] at this index.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.title.text = item.title

        // Visibility Logic: Recent searches (MODE_HISTORY) do not display pricing
        if (displayMode != MODE_HISTORY) {
            holder.price?.text = item.price
            holder.price?.visibility = View.VISIBLE
        } else {
            holder.price?.visibility = View.GONE
        }

        // Set the product or placeholder image
        holder.productImage?.setImageResource(item.imageResId)

        // Set up interaction listener for the root view of the item
        holder.itemView.setOnClickListener {
            onClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int = items.size

    /**
     * Replaces the current dataset and triggers a full UI refresh.
     * Note: Uses toList() to create a defensive copy of the provided list.
     */
    fun updateList(newList: List<Item>) {
        items = newList.toList()
        notifyDataSetChanged()
    }
}