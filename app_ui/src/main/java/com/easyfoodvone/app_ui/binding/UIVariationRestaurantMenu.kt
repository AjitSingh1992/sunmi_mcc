package com.easyfoodvone.app_ui.binding

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageRestaurantMenu
import com.easyfoodvone.app_ui.databinding.PageRestaurantMenuBinding as TabletType
import com.easyfoodvone.app_ui.databinding.PageRestaurantMenuPhoneBinding as PhoneType

sealed class UIVariationRestaurantMenu<T : ViewDataBinding> {
    abstract val binding: T
    abstract val recycler: RecyclerView
    abstract fun setData(data: DataPageRestaurantMenu, lifecycle: LifecycleSafe)

    class Phone(override val binding: PhoneType): UIVariationRestaurantMenu<PhoneType>() {
        override val recycler = binding.recycler
        override fun setData(data: DataPageRestaurantMenu, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: TabletType): UIVariationRestaurantMenu<TabletType>() {
        override val recycler = binding.recycler
        override fun setData(data: DataPageRestaurantMenu, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}