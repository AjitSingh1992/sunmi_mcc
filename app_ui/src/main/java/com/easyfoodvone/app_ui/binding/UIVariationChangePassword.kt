package com.easyfoodvone.app_ui.binding

import android.widget.EditText
import androidx.databinding.ViewDataBinding
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageChangePassword
import com.easyfoodvone.app_ui.databinding.PageChangePasswordBinding
import com.easyfoodvone.app_ui.databinding.PageChangePasswordPhoneBinding

sealed class UIVariationChangePassword<T : ViewDataBinding> {
    abstract val binding: T
    abstract val edt_new_password: EditText
    abstract val edt_confirm_paswword: EditText
    abstract val edit_current_password: EditText
    abstract fun setData(data: DataPageChangePassword, lifecycle: LifecycleSafe)

    class Phone(override val binding: PageChangePasswordPhoneBinding) : UIVariationChangePassword<PageChangePasswordPhoneBinding>() {
        override val edt_new_password = binding.editNewPassword
        override val edt_confirm_paswword = binding.editConfirmPassword
        override val edit_current_password = binding.editCurrentPassword
        override fun setData(data: DataPageChangePassword, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }

    class Tablet(override val binding: PageChangePasswordBinding) : UIVariationChangePassword<PageChangePasswordBinding>() {
        override val edt_new_password = binding.editNewPassword
        override val edt_confirm_paswword = binding.editConfirmPassword
        override val edit_current_password = binding.editCurrentPassword
        override fun setData(data: DataPageChangePassword, lifecycle: LifecycleSafe) {
            binding.data = data
            lifecycle.unbindOnDestroy(::binding)
        }
    }
}