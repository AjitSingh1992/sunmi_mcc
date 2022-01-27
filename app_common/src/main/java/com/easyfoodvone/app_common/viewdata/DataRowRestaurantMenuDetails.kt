package com.easyfoodvone.app_common.viewdata

import androidx.recyclerview.widget.RecyclerView
import com.easyfoodvone.app_common.separation.ObservableField

class DataRowRestaurantMenuDetails(
        val showDragHandle: ObservableField<Boolean>,
        val name: ObservableField<String>,
        val nameExtra: ObservableField<String?>,
        val price: ObservableField<String?>,
        val toggleIsOn: ObservableField<Boolean>,
        val toggleUI: ObservableField<ToggleUI>,
        val isEditClickable: ObservableField<Boolean>,
        val dragInProgress: ObservableField<Boolean>,
        val outputEvents: OutputEvents) {

    enum class ToggleUI {
        CLICKABLE_IDLE,
        CLICKABLE_LOADING_WS,
        UNCLICKABLE_IDLE
    }

    interface OutputEvents {
        fun bind(adapterPosition: Int)
        fun onClickToggle()
        fun onClickEdit()
        fun onTouchDownDragHandle(viewHolder: RecyclerView.ViewHolder)
    }
}