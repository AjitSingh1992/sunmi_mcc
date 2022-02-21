package com.easyfoodvone.app_common.viewdata

import com.easyfoodvone.app_common.separation.ObservableArrayListMoveable
import com.easyfoodvone.app_common.ws.MenuCategoryList

class DataPageRestaurantMenu(
        val menuItems: ObservableArrayListMoveable<MenuCategoryList.MenuCategories>,
        val outputEvents: OutputEvents) {

    interface OutputEvents {
        fun openMenuDetails(item: MenuCategoryList.MenuCategories)
        fun onItemMove(fromPosition: Int, toPosition: Int)
        fun onItemMoveDone()
    }
}