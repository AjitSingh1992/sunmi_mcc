package com.easyfoodvone.app_common.viewdata

import com.easyfoodvone.app_common.separation.ObservableArrayListMoveable
import com.easyfoodvone.app_common.separation.ObservableField
import com.easyfoodvone.app_common.ws.MenuCategoryItemsResponse

class DataPageRestaurantMenuDetails(
        val categoryRow: DataRowRestaurantMenuDetails,
        val menuItems: ObservableArrayListMoveable<MenuCategoryItemsResponse.Items>,
        val mealItems: ObservableArrayListMoveable<MenuCategoryItemsResponse.Meals>,
        val childDialogVisible: ObservableField<Boolean>,
        val outputEvents: OutputEvents) {

    interface OutputEvents {
        fun onSetProductActive(item: MenuCategoryItemsResponse.Items, isActive: Boolean)
        fun onSetMealProductActive(item: MenuCategoryItemsResponse.Meals, isActive: Boolean)
        fun onEditClicked(items: MenuCategoryItemsResponse.Items)
        fun onMealEditClicked(items: MenuCategoryItemsResponse.Meals)
        fun onItemMove(fromPosition: Int, toPosition: Int)
        fun onMealItemMove(fromPosition: Int, toPosition: Int)
        fun onItemMoveDone()
        fun onMealItemMoveDone()

    }
}