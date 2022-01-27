package com.easyfoodvone.app_ui.binding

import android.widget.ImageView
import androidx.databinding.ViewDataBinding
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataRowRestaurantMenuDetails
import com.easyfoodvone.app_ui.view.ViewRestaurantMenuDetail
import com.easyfoodvone.app_ui.databinding.RowRestaurantMenuDetailsBinding as TabletType
import com.easyfoodvone.app_ui.databinding.RowRestaurantMenuDetailsPhoneBinding as PhoneType

sealed class UIVariationRestaurantMenuDetailsItemRow<T : ViewDataBinding> {
    abstract val binding: T
    abstract val dragHandle: ImageView
    abstract fun setData(data: DataRowRestaurantMenuDetails, formatter: ViewRestaurantMenuDetail.Formatter, lifecycle: LifecycleSafe)

    class Phone(override val binding: PhoneType) : UIVariationRestaurantMenuDetailsItemRow<PhoneType>() {
        override val dragHandle = binding.dragHandle
        override fun setData(data: DataRowRestaurantMenuDetails, formatter: ViewRestaurantMenuDetail.Formatter, lifecycle: LifecycleSafe) {
            binding.data = data
            binding.formatter = formatter
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: TabletType) : UIVariationRestaurantMenuDetailsItemRow<TabletType>() {
        override val dragHandle = binding.dragHandle
        override fun setData(data: DataRowRestaurantMenuDetails, formatter: ViewRestaurantMenuDetail.Formatter, lifecycle: LifecycleSafe) {
            binding.data = data
            binding.formatter = formatter
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}