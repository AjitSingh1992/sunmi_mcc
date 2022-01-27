package com.easyfoodvone.app_ui.binding

import android.widget.EditText
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageDeliverySettingsAndPostcodes
import com.easyfoodvone.app_ui.databinding.PageDeliverySettingsAndPostcodesBinding
import com.easyfoodvone.app_ui.databinding.PageDeliverySettingsAndPostcodesPhoneBinding

sealed class UIVariationDeliverySettingsAndPostcode<T : ViewDataBinding> {
    abstract val binding: T
    abstract val distance: TextView
    abstract val recyclerTimeList: RecyclerView
    abstract val etDeliveryCharge: EditText
    abstract val etMinimumOrder: EditText
    abstract val etFreeDelivery: EditText
    abstract val deliveryTime: EditText
    abstract val etAvgPrepTime: EditText
    abstract fun setData(data: DataPageDeliverySettingsAndPostcodes, lifecycle: LifecycleSafe)

    class Phone(override val binding: PageDeliverySettingsAndPostcodesPhoneBinding): UIVariationDeliverySettingsAndPostcode<PageDeliverySettingsAndPostcodesPhoneBinding>() {
        override val distance = binding.distance
        override val recyclerTimeList = binding.recyclerTimeList
        override val etDeliveryCharge = binding.etDeliveryCharge
        override val etMinimumOrder = binding.etMinimumOrder
        override val etFreeDelivery = binding.etFreeDelivery
        override val deliveryTime = binding.deliveryTime
        override val etAvgPrepTime = binding.etAvgPrepTime
        override fun setData(data: DataPageDeliverySettingsAndPostcodes, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: PageDeliverySettingsAndPostcodesBinding): UIVariationDeliverySettingsAndPostcode<PageDeliverySettingsAndPostcodesBinding>() {
        override val distance: TextView = binding.distance
        override val recyclerTimeList = binding.recyclerTimeList
        override val etDeliveryCharge = binding.etDeliveryCharge
        override val etMinimumOrder = binding.etMinimumOrder
        override val etFreeDelivery = binding.etFreeDelivery
        override val deliveryTime = binding.deliveryTime
        override val etAvgPrepTime = binding.etAvgPrepTime
        override fun setData(data: DataPageDeliverySettingsAndPostcodes, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}