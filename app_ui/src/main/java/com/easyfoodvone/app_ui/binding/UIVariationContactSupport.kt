package com.easyfoodvone.app_ui.binding

import androidx.databinding.ViewDataBinding
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageContactSupport
import com.easyfoodvone.app_ui.databinding.PageContactSupportBinding as TabletType
import com.easyfoodvone.app_ui.databinding.PageContactSupportPhoneBinding as PhoneType

sealed class UIVariationContactSupport<T : ViewDataBinding> {
    abstract val binding: T
    abstract fun setData(data: DataPageContactSupport, lifecycle: LifecycleSafe)

    class Phone(override val binding: PhoneType): UIVariationContactSupport<PhoneType>() {
        override fun setData(data: DataPageContactSupport, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: TabletType): UIVariationContactSupport<TabletType>() {
        override fun setData(data: DataPageContactSupport, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}