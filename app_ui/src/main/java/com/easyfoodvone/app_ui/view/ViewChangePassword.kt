package com.easyfoodvone.app_ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageChangePassword
import com.easyfoodvone.app_ui.R
import com.easyfoodvone.app_ui.binding.UIVariationChangePassword

class ViewChangePassword(val lifecycle: LifecycleSafe, val isPhone: Boolean) {

    private var ui: UIVariationChangePassword<*>? = null

    fun getRoot(): View {
        return ui!!.binding.root
    }

    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, data: DataPageChangePassword) {
        ui = if (isPhone) {
            UIVariationChangePassword.Phone(
                DataBindingUtil.inflate(inflater, R.layout.page_change_password_phone, container, false))
        } else {
            UIVariationChangePassword.Tablet(
                DataBindingUtil.inflate(inflater, R.layout.page_change_password, container, false))

        }.apply {
            setData(data, lifecycle)
        }

        lifecycle.setValueUntilDestroy(data.inputEvents, inputEventHandler)

        // Prevent memory leak
        lifecycle.nullOnDestroy(::ui)
    }

    private val inputEventHandler = object : DataPageChangePassword.InputEvents {
        override fun setErrorCurrentPassword(msg: String) {
            ui!!.edit_current_password.setError(msg)
        }

        override fun setErrorNewPassword(msg: String) {
            ui!!.edt_new_password.setError(msg)
        }

        override fun setErrorConfirmPassword(msg: String) {
            ui!!.edt_confirm_paswword.setError(msg);
        }
    }
}