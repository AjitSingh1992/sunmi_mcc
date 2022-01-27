package com.easyfoodvone.app_ui.binding

import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageForgotPassword
import com.easyfoodvone.app_ui.databinding.PageForgotPasswordBinding as TabletType
import com.easyfoodvone.app_ui.databinding.PageForgotPasswordPhoneBinding as PhoneType

sealed class UIVariationForgotPassword<T : ViewDataBinding> {
    abstract val binding: T
    abstract val textError: TextView
    abstract fun setData(data: DataPageForgotPassword, lifecycle: LifecycleSafe)

    class Phone(override val binding: PhoneType): UIVariationForgotPassword<PhoneType>() {
        override val textError = binding.textError
        override fun setData(data: DataPageForgotPassword, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: TabletType): UIVariationForgotPassword<TabletType>() {
        override val textError = binding.textError
        override fun setData(data: DataPageForgotPassword, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}