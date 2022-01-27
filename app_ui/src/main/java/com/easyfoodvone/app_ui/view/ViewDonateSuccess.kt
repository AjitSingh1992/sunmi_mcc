package com.easyfoodvone.app_ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.easyfoodvone.app_common.separation.LifecycleSafe
import com.easyfoodvone.app_common.viewdata.DataPageDonateSuccess
import com.easyfoodvone.app_ui.R
import com.easyfoodvone.app_ui.binding.UIVariationDonateSuccess

class ViewDonateSuccess(val lifecycle: LifecycleSafe, val isPhone: Boolean) {

    var ui: UIVariationDonateSuccess<*>? = null
    private var data: DataPageDonateSuccess? = null

    fun onCreateView(data: DataPageDonateSuccess, inflater: LayoutInflater, container: ViewGroup?) {
        this.data = data

        ui = if (isPhone) {
            UIVariationDonateSuccess.Phone(
                DataBindingUtil.inflate(inflater, R.layout.page_donate_success_phone, container, false))
        } else {
            UIVariationDonateSuccess.Tablet(
                DataBindingUtil.inflate(inflater, R.layout.page_donate_success, container, false))

        }.apply {
            setData(data, lifecycle)
        }

        // Prevent memory leak
        lifecycle.nullOnDestroy(::ui)
    }
}