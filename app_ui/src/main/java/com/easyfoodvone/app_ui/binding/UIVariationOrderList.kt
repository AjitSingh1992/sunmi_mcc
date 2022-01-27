package com.easyfoodvone.app_ui.binding

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageOrderList
import com.easyfoodvone.app_ui.databinding.PageOrderListPhoneBinding as PhoneType
import com.easyfoodvone.app_ui.databinding.PageOrderListTabletBinding as TabletType

/**
 * Common interface for swapping tablet and phone layout bindings which are largely identical in terms of view IDs
 */
sealed class UIVariationOrderList<T : ViewDataBinding> {
    abstract val binding: T
    abstract val swipeRefresh: SwipeRefreshLayout
    abstract val recyclerOrdersList: RecyclerView
    abstract fun setData(data: DataPageOrderList, lifecycle: LifecycleSafe)

    class Phone(override val binding: PhoneType) : UIVariationOrderList<PhoneType>() {
        override val swipeRefresh = binding.swipeRefresh
        override val recyclerOrdersList = binding.recyclerOrdersList
        override fun setData(data: DataPageOrderList, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: TabletType) : UIVariationOrderList<TabletType>() {
        override val swipeRefresh = binding.swipeRefresh
        override val recyclerOrdersList = binding.recyclerOrdersList
        override fun setData(data: DataPageOrderList, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}