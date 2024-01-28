package com.example.myshopnetapp.Utilities

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalItemDecoration (
    // Set the amount of space to add as the constructor
    private val amount: Int = 30):RecyclerView.ItemDecoration() {

    // Override the getItemOffsets method to add spacing between items
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        // Add the defined amount of space to the bottom of each item
        outRect.bottom = amount
    }
}