package com.sergeenko.alexey.noble.utills

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacesItemDecoration(private val spanCount: Int, private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column: Int = position % spanCount // item column

        outRect.left = column * spacing / spanCount
        outRect.right = spacing - (column + 1) * spacing / spanCount
    }
}