package com.intprog.ebay

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(
    private var items: List<Item>,
    private val displayMode: Int = MODE_SEARCH,
    private val onClick: ((Item) -> Unit)? = null
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    companion object {
        const val MODE_SEARCH = 0      // Standard search result (Vertical)
        const val MODE_HISTORY = 1     // Recent search text (Vertical)
        const val MODE_MINI_CARD = 2   // Recently Viewed items (Horizontal)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.itemTitle)
        val price: TextView? = view.findViewById(R.id.itemPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = when (displayMode) {
            MODE_HISTORY -> R.layout.item_recent_search
            MODE_MINI_CARD -> R.layout.item_recent // Use the fixed-width layout
            else -> R.layout.item_search
        }

        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title

        // Hide price only for text-only history mode
        if (displayMode != MODE_HISTORY) {
            holder.price?.text = item.price
            holder.price?.visibility = View.VISIBLE
        } else {
            holder.price?.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newList: List<Item>) {
        items = newList.toList()
        notifyDataSetChanged()
    }
}