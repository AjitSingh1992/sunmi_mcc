package com.easyfoodvone.app_ui.binding

import androidx.databinding.ViewDataBinding
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataDialogRestaurantTime
import com.easyfoodvone.app_ui.databinding.PopupSetRestaurentTimeBinding as TabletType
import com.easyfoodvone.app_ui.databinding.PopupSetRestaurentTimePhoneBinding as PhoneType

sealed class UIVariationPopupSetRestaurentTime<T : ViewDataBinding> {
    abstract val binding: T
    abstract fun setData(data: DataDialogRestaurantTime, lifecycle: LifecycleSafe)

    class Phone(override val binding: PhoneType) : UIVariationPopupSetRestaurentTime<PhoneType>() {
        override fun setData(data: DataDialogRestaurantTime, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: TabletType) : UIVariationPopupSetRestaurentTime<TabletType>() {
        override fun setData(data: DataDialogRestaurantTime, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}