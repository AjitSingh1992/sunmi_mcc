package com.easyfoodvone.app_common.viewdata

import androidx.recyclerview.widget.RecyclerView
import com.easyfoodvone.app_common.separation.ObservableField

class DataRowRestaurantMenu(
        //val imageUrl: ObservableField<String>,
        val title: ObservableField<String>,
        val itemCount: ObservableField<String>,
        val dragInProgress: ObservableField<Boolean>,
        val outputEvents: OutputEvents) {

    interface OutputEvents {
        fun onDragToReorder(viewHolder: RecyclerView.ViewHolder)
        fun onClickCategory(adapterPosition: Int)
    }
}