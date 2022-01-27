package com.easyfoodvone.app_ui.binding

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageRestaurantTimings
import com.easyfoodvone.app_ui.databinding.PageRestaurantTimingsBinding
import com.easyfoodvone.app_ui.databinding.PageRestaurantTimingsPhoneBinding

sealed class UIVariationRestaurantTimings<T : ViewDataBinding> {
    abstract val listTimings: RecyclerView
    abstract val binding: T
    abstract fun setData(data: DataPageRestaurantTimings, lifecycle: LifecycleSafe)

    class Phone(override val binding: PageRestaurantTimingsPhoneBinding): UIVariationRestaurantTimings<PageRestaurantTimingsPhoneBinding>() {
        override val listTimings = binding.listTimings
        override fun setData(data: DataPageRestaurantTimings, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: PageRestaurantTimingsBinding): UIVariationRestaurantTimings<PageRestaurantTimingsBinding>() {
        override val listTimings = binding.listTimings
        override fun setData(data: DataPageRestaurantTimings, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}