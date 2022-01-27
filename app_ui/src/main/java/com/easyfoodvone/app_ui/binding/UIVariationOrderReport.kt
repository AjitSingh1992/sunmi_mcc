package com.easyfoodvone.app_ui.binding

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageOrderReport
import com.easyfoodvone.app_ui.databinding.PageOrderReportBinding as TabletType
import com.easyfoodvone.app_ui.databinding.PageOrderReportPhoneBinding as PhoneType

sealed class UIVariationOrderReport<T : ViewDataBinding> {
    abstract val binding: T
    abstract val totalOrdersList: RecyclerView
    abstract fun setData(data: DataPageOrderReport, lifecycle: LifecycleSafe)

    class Phone(override val binding: PhoneType) : UIVariationOrderReport<PhoneType>() {
        override val totalOrdersList = binding.totalOrdersList
        override fun setData(data: DataPageOrderReport, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: TabletType) : UIVariationOrderReport<TabletType>() {
        override val totalOrdersList = binding.totalOrdersList
        override fun setData(data: DataPageOrderReport, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}