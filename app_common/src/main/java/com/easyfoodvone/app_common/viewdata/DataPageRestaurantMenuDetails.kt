package com.easyfoodvone.app_common.viewdata

import com.easyfoodvone.app_common.separation.ObservableArrayListMoveable
import com.easyfoodvone.app_common.separation.ObservableField
import com.easyfoodvone.app_common.ws.MenuCategoryItemsResponse

class DataPageRestaurantMenuDetails(
        val categoryRow: DataRowRestaurantMenuDetails,
        val menuItems: ObservableArrayListMoveable<MenuCategoryItemsResponse.Items>,
        val childDialogVisible: ObservableField<Boolean>,
        val outputEvents: OutputEvents) {

    interface OutputEvents {
        fun onSetProductActive(item: MenuCategoryItemsResponse.Items, isActive: Boolean)
        fun onEditClicked(items: MenuCategoryItemsResponse.Items)
        fun onItemMove(fromPosition: Int, toPosition: Int)
        fun onItemMoveDone()
    }
}