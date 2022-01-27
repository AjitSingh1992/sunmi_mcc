package com.easyfoodvone.app_ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageDonateCancel
import com.easyfoodvone.app_ui.R
import com.easyfoodvone.app_ui.binding.UIVariationDonateCancel

class ViewDonateCancel(val lifecycle: LifecycleSafe, val isPhone: Boolean) {

    var ui: UIVariationDonateCancel<*>? = null
    private var data: DataPageDonateCancel? = null

    fun onCreateView(data: DataPageDonateCancel, inflater: LayoutInflater, container: ViewGroup?) {
        this.data = data

        ui = if (isPhone) {
            UIVariationDonateCancel.Phone(
                DataBindingUtil.inflate(inflater, R.layout.page_donate_cancel_phone, container, false))
        } else {
            UIVariationDonateCancel.Tablet(
                DataBindingUtil.inflate(inflater, R.layout.page_donate_cancel, container, false))

        }.apply {
            setData(data, lifecycle)
        }

        // Prevent memory leak
        lifecycle.nullOnDestroy(::ui)
    }
}