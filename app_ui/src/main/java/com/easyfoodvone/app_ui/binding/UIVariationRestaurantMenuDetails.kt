package com.easyfoodvone.app_ui.binding

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageRestaurantMenuDetails
import com.easyfoodvone.app_ui.view.ViewRestaurantMenuDetail
import com.easyfoodvone.app_ui.databinding.PageRestaurantMenuDetailsBinding as TabletType
import com.easyfoodvone.app_ui.databinding.PageRestaurantMenuDetailsPhoneBinding as PhoneType

sealed class UIVariationRestaurantMenuDetails<T : ViewDataBinding> {
    abstract val binding: T
    abstract val recycler: RecyclerView
    abstract val childFragmentLayout: ViewGroup
    abstract fun setData(data: DataPageRestaurantMenuDetails, formatter: ViewRestaurantMenuDetail.Formatter, lifecycle: LifecycleSafe)

    class Phone(override val binding: PhoneType) : UIVariationRestaurantMenuDetails<PhoneType>() {
        override val recycler = binding.recycler
        override val childFragmentLayout = binding.childFragmentLayout
        override fun setData(data: DataPageRestaurantMenuDetails, formatter: ViewRestaurantMenuDetail.Formatter, lifecycle: LifecycleSafe) {
            binding.data = data
            binding.formatter = formatter
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: TabletType) : UIVariationRestaurantMenuDetails<TabletType>() {
        override val recycler = binding.recycler
        override val childFragmentLayout = binding.childFragmentLayout
        override fun setData(data: DataPageRestaurantMenuDetails, formatter: ViewRestaurantMenuDetail.Formatter, lifecycle: LifecycleSafe) {
            binding.data = data
            binding.formatter = formatter
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}