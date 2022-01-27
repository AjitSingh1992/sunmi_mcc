package com.easyfoodvone.app_ui.binding

import androidx.databinding.ViewDataBinding
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageRevenueReport
import com.easyfoodvone.app_ui.databinding.PageRevenueReportBinding as TabletType
import com.easyfoodvone.app_ui.databinding.PageRevenueReportPhoneBinding as PhoneType

sealed class UIVariationRevenueReport<T : ViewDataBinding> {
    abstract val binding: T
    abstract fun setData(data: DataPageRevenueReport, lifecycle: LifecycleSafe)

    class Phone(override val binding: PhoneType) : UIVariationRevenueReport<PhoneType>() {
        override fun setData(data: DataPageRevenueReport, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: TabletType) : UIVariationRevenueReport<TabletType>() {
        override fun setData(data: DataPageRevenueReport, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}