package com.intprog.ebay

data class Item(
    val title: String,
    val price: String,
    val imageResId: Int = android.R.drawable.ic_menu_gallery
)