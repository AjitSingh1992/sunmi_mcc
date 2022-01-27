package com.easyfoodvone.app_ui.binding

import androidx.databinding.ViewDataBinding
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageDonateSuccess
import com.easyfoodvone.app_ui.databinding.PageDonateSuccessBinding as TabletType
import com.easyfoodvone.app_ui.databinding.PageDonateSuccessPhoneBinding as PhoneType

sealed class UIVariationDonateSuccess<T : ViewDataBinding> {
    abstract val binding: T
    abstract fun setData(data: DataPageDonateSuccess, lifecycle: LifecycleSafe)

    class Phone(override val binding: PhoneType): UIVariationDonateSuccess<PhoneType>() {
        override fun setData(data: DataPageDonateSuccess, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: TabletType): UIVariationDonateSuccess<TabletType>() {
        override fun setData(data: DataPageDonateSuccess, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}