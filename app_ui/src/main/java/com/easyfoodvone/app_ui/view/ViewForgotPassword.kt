package com.easyfoodvone.app_ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.OnRebindCallback
import androidx.databinding.ViewDataBinding
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageForgotPassword
import com.easyfoodvone.app_ui.R
import com.easyfoodvone.app_ui.binding.UIVariationForgotPassword

class ViewForgotPassword(val lifecycle: LifecycleSafe, val isPhone: Boolean) {

    private var ui: UIVariationForgotPassword<*>? = null

    fun getRoot(): View {
        return ui!!.binding.root
    }

    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, data: DataPageForgotPassword) {
        ui = if (isPhone) {
            UIVariationForgotPassword.Phone(
                    DataBindingUtil.inflate(inflater, R.layout.page_forgot_password_phone, container, false))
        } else {
            UIVariationForgotPassword.Tablet(
                    DataBindingUtil.inflate(inflater, R.layout.page_forgot_password, container, false))

        }.apply {
            setData(data, lifecycle)
            binding.addOnRebindCallback(onRebindCallback)
        }

        // Prevent memory leak
        lifecycle.nullOnDestroy(::ui)
    }

    private val onRebindCallback = object : OnRebindCallback<ViewDataBinding>() {
        override fun onPreBind(untypedBinding: ViewDataBinding?): Boolean {
            val fade = Fade()
            fade.addTarget(ui!!.textError)

            TransitionManager.beginDelayedTransition(ui!!.binding.root as ViewGroup, fade)
            return true
        }
    }
}