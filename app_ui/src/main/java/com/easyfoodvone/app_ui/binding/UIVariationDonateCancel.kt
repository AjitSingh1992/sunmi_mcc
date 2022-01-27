package com.easyfoodvone.app_ui.binding

import androidx.databinding.ViewDataBinding
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageDonateCancel
import com.easyfoodvone.app_ui.databinding.PageDonateCancelBinding as TabletType
import com.easyfoodvone.app_ui.databinding.PageDonateCancelPhoneBinding as PhoneType

sealed class UIVariationDonateCancel<T : ViewDataBinding> {
    abstract val binding: T
    abstract fun setData(data: DataPageDonateCancel, lifecycle: LifecycleSafe)

    class Phone(override val binding: PhoneType) : UIVariationDonateCancel<PhoneType>() {
        override fun setData(data: DataPageDonateCancel, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: TabletType) : UIVariationDonateCancel<TabletType>() {
        override fun setData(data: DataPageDonateCancel, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}