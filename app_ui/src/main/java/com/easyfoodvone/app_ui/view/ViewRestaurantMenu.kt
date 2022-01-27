package com.easyfoodvone.app_ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageRestaurantMenu
import com.easyfoodvone.app_common.ws.MenuCategoryList
import com.easyfoodvone.app_ui.adapter.MenuGridAdapter
import com.easyfoodvone.app_ui.adapter.ItemTouchHelperAdapter
import com.easyfoodvone.app_ui.adapter.SimpleItemTouchHelperCallback
import com.easyfoodvone.app_ui.binding.UIVariationRestaurantMenu
import com.easyfoodvone.app_ui.databinding.PageRestaurantMenuBinding
import com.easyfoodvone.app_ui.databinding.PageRestaurantMenuPhoneBinding

class ViewRestaurantMenu(val lifecycle: LifecycleSafe, val isPhone: Boolean, val data: DataPageRestaurantMenu) {

    private var ui: UIVariationRestaurantMenu<*>? = null
    private var itemTouchHelper: ItemTouchHelper? = null

    fun getRoot(): View {
        return ui!!.binding.root
    }

    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?) {
        ui = if (isPhone) {
            UIVariationRestaurantMenu.Phone(PageRestaurantMenuPhoneBinding.inflate(inflater, container, false))
        } else {
            UIVariationRestaurantMenu.Tablet(PageRestaurantMenuBinding.inflate(inflater, container, false))

        }.apply {
            setData(data, lifecycle)

            val adapter = MenuGridAdapter(adapterParentInterface, lifecycle, data.menuItems, inflater, isPhone)
            recycler.setAdapter(adapter)
            lifecycle.addObserverOnceUntilDestroy(data.menuItems, adapter, callNow=false)

            itemTouchHelper = ItemTouchHelper(
                object : SimpleItemTouchHelperCallback(touchHelperItemCallback) {
                    override fun isLongPressDragEnabled(): Boolean {
                        // We use a "drag handle" on each item view, disable the automatic full-item drag target
                        return false
                    }

                }).apply {
                    attachToRecyclerView(recycler)
                }
        }

        // Prevent view memory leak
        lifecycle.nullOnDestroy(::ui)
        lifecycle.nullOnDestroy(::itemTouchHelper)
    }

    private val adapterParentInterface = object : MenuGridAdapter.ParentInterface {
        override fun openMenuDetails(item: MenuCategoryList.MenuCategories) {
            data.outputEvents.openMenuDetails(item)
        }

        override fun startReorderDrag(item: RecyclerView.ViewHolder) {
            itemTouchHelper?.startDrag(item)
        }
    }

    private val touchHelperItemCallback = object : ItemTouchHelperAdapter {
        override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
            data.outputEvents.onItemMove(fromPosition, toPosition)

            return true
        }

        override fun onItemDismiss(position: Int) { }
    }
}